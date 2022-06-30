package com.morpheusdata.vault.util
import com.morpheusdata.core.util.ConnectionUtils
import com.morpheusdata.core.util.HttpApiClient
import com.morpheusdata.response.ServiceResponse

abstract class AbstractVaultEngine implements VaultEngineInterface {
  
  private String code

  public static final String DEFAULT_API_VERSION = "v1"

  public abstract ServiceResponse delete(String vaultPath, String vaultUrl, String vaultToken)

  public abstract ServiceResponse read(String vaultPath, String vaultUrl, String vaultToken)

  public abstract ServiceResponse save(String vaultPath, Map value, String vaultUrl, String vaultToken)
  
  public abstract String getDescription()
  
  public abstract String getName()
  
  public abstract String getDefaultSecretMount()
  
  AbstractVaultEngine(String code) {
    this.code = code
  }
  
  public String getCode() {
    return this.code
  }
    
  private String getBasePath(String version) {
    if (version == null) {
      version = AbstractVaultEngine.DEFAULT_API_VERSION
    }
    return "/" + version + "/"
  }

  private ServiceResponse _delete(String vaultPath, String vaultUrl, String vaultToken) {
    HttpApiClient apiClient = new HttpApiClient()
    HttpApiClient.RequestOptions restOptions = this.createRestApiOptions(vaultToken)
    vaultPath = this.getBasePath() + vaultPath
    ServiceResponse response
    try {
      def apiResults = apiClient.callJsonApi(vaultUrl,vaultPath,null,null,restOptions,"DELETE")
      if(apiResults.success) {
        response = ServiceResponse.success(apiResults.data as Map)
      } else {
        response = ServiceResponse.error(apiResults.error)
      }
    } catch(Exception ex) {
      response = ServiceResponse.error("An error occured deleting the secret from HashiCorp Vault")
    } finally {
      apiClient.shutdownClient()
    }
    return response
  }

  private ServiceResponse _save(String vaultPath, Map body, String vaultUrl, String vaultToken) {
    HttpApiClient apiClient = new HttpApiClient()
    ServiceResponse response
    HttpApiClient.RequestOptions restOptions = this.createRestApiOptions(vaultToken)
    vaultPath = this.getBasePath() + vaultPath
    restOptions.body = body
    try {
      def apiResults = apiClient.callJsonApi(vaultUrl, vaultPath, null, null, restOptions, "POST")
      if(apiResults.success) {
        response = ServiceResponse.success(apiResults.data as Map)
      } else {
        response = ServiceResponse.error(apiResults.error)
      }
    } catch(Exception ex) {
      response = ServiceResponse.error("An error occured saving the secret from HashiCorp Vault")
    } finally {
      apiClient.shutdownClient()
    }
    return response
  }

  private ServiceResponse _read(String vaultPath, String vaultUrl, String vaultToken) {
    HttpApiClient apiClient = new HttpApiClient()
    HttpApiClient.RequestOptions restOptions = this.createRestApiOptions(vaultToken)
    vaultPath = this.getBasePath() + vaultPath
    ServiceResponse response
    try {
      def apiResults = apiClient.callJsonApi(vaultUrl, vaultPath, null, null, restOptions, "GET")
      if (apiResults.success) {
        response = ServiceResponse.success(apiResults.data as Map)
      } else {
        response = ServiceResponse.error(apiResults.error)
      }
    } catch(Exception ex) {
      response = ServiceResponse.error("An error occured reading the secret from HashiCorp Vault")
    } finally {
      apiClient.shutdownClient()
    }
    return response
  }

  private HttpApiClient.RequestOptions createRestApiOptions(String vaultToken) {
    HttpApiClient.RequestOptions restOptions = new HttpApiClient.RequestOptions()
    restOptions.headers = new LinkedHashMap<>()
    this.setAuthenticationHeaders(restOptions, vaultToken)
    restOptions.contentType = "application/json"
    return restOptions
  }

  private setAuthenticationHeaders(HttpApiClient.RequestOptions restOptions, String vaultToken) {
    restOptions.headers.put("X-VAULT-TOKEN", vaultToken)
  }
}