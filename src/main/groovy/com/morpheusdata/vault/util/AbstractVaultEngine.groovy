package com.morpheusdata.vault.util
import com.morpheusdata.core.util.ConnectionUtils
import com.morpheusdata.core.util.HttpApiClient
import com.morpheusdata.response.ServiceResponse
import com.morpheusdata.core.MorpheusContext

abstract class AbstractVaultEngine implements VaultEngineInterface {
  
  private String code

  public static final String DEFAULT_API_VERSION = "v1"

  public abstract ServiceResponse delete(String vaultPath, String vaultUrl, String vaultToken, MorpheusContext morpheusContext)

  public abstract ServiceResponse read(String vaultPath, String vaultUrl, String vaultToken, MorpheusContext morpheusContext)

  public abstract ServiceResponse save(String vaultPath, Map value, String vaultUrl, String vaultToken, MorpheusContext morpheusContext)
      
  public abstract String getDescription()
  
  public abstract String getName()
  
  public abstract String getDefaultEngineMount()
  
  AbstractVaultEngine(String code) {
    this.code = code
  }
  
  public String getCode() {
    return this.code
  }
  
  public String getFullVaultPath(String engineMount, String secretPathSuffix, String name, Map opts = [:]) {
    if(secretPathSuffix.endsWith('/')) {
      return engineMount + "/" + secretPathSuffix + name
    } else {
      return engineMount + "/" + secretPathSuffix + "/" + name
    }
  }
  
  public ServiceResponse checkHealth(String vaultUrl, MorpheusContext morpheusContext) {
    HttpApiClient apiClient = new HttpApiClient()
    apiClient.networkProxy = morpheusContext.services.setting.getGlobalNetworkProxy()
    try {
      def apiResults = apiClient.callJsonApi(vaultUrl,'/v1/sys/health',new HttpApiClient.RequestOptions(),'GET')
      if(apiResults.success) {
        ServiceResponse<Map> response = new ServiceResponse<>(true,null,null,[:])
        return response
      } else {
        return ServiceResponse.error(apiResults.error,null,[:])
      }
    } finally {
      apiClient.shutdownClient()
    }
  }
    
  private String getBasePath(String version) {
    if (version == null) {
      version = AbstractVaultEngine.DEFAULT_API_VERSION
    }
    return "/" + version + "/"
  }

  private ServiceResponse _delete(String vaultPath, String vaultUrl, String vaultToken, MorpheusContext morpheusContext) {
    HttpApiClient apiClient = new HttpApiClient()
    apiClient.networkProxy = morpheusContext.services.setting.getGlobalNetworkProxy()
    HttpApiClient.RequestOptions restOptions = this.createRestApiOptions(vaultToken)
    vaultPath = this.getBasePath() + vaultPath
    ServiceResponse response
    try {
      def apiResults = apiClient.callJsonApi(vaultUrl,vaultPath,null,null,restOptions,"DELETE")
      if(apiResults.success) {
        response = ServiceResponse.success(apiResults.data as Map)
      } else {
        response = ServiceResponse.error(apiResults.error ?: apiResults.content ?: "An unknown error occured deleting the secret from HashiCorp Vault")
      }
    } catch(Exception ex) {
      response = ServiceResponse.error("An error occured deleting the secret from HashiCorp Vault")
    } finally {
      apiClient.shutdownClient()
    }
    return response
  }

  private ServiceResponse _save(String vaultPath, Map body, String vaultUrl, String vaultToken, MorpheusContext morpheusContext) {
    HttpApiClient apiClient = new HttpApiClient()
    apiClient.networkProxy = morpheusContext.services.setting.getGlobalNetworkProxy()
    ServiceResponse response
    HttpApiClient.RequestOptions restOptions = this.createRestApiOptions(vaultToken)
    vaultPath = this.getBasePath() + vaultPath
    restOptions.body = body
    try {
      def apiResults = apiClient.callJsonApi(vaultUrl, vaultPath, null, null, restOptions, "POST")
      if(apiResults.success) {
        response = ServiceResponse.success(apiResults.data as Map)
      } else {
        response = ServiceResponse.error(apiResults.error ?: apiResults.content ?: "An unknown error occured saving the secret to HashiCorp Vault")
      }
    } catch(Exception ex) {
      response = ServiceResponse.error("An error occured saving the secret from HashiCorp Vault")
    } finally {
      apiClient.shutdownClient()
    }
    return response
  }

  private ServiceResponse _read(String vaultPath, String vaultUrl, String vaultToken, MorpheusContext morpheusContext) {
    HttpApiClient apiClient = new HttpApiClient()
    apiClient.networkProxy = morpheusContext.services.setting.getGlobalNetworkProxy()
    HttpApiClient.RequestOptions restOptions = this.createRestApiOptions(vaultToken)
    vaultPath = this.getBasePath() + vaultPath
    ServiceResponse response
    try {
      def apiResults = apiClient.callJsonApi(vaultUrl, vaultPath, null, null, restOptions, "GET")
      if (apiResults.success) {
        response = ServiceResponse.success(apiResults.data as Map)
      } else {
        response = ServiceResponse.error(apiResults.error ?: apiResults.content ?: "An unknown error occured reading the secret from HashiCorp Vault")
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