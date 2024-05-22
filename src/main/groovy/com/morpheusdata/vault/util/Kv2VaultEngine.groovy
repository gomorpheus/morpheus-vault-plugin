package com.morpheusdata.vault.util
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.response.ServiceResponse

class Kv2VaultEngine extends AbstractVaultEngine {

  Kv2VaultEngine(String code) {super(code)}

  public ServiceResponse delete(String vaultPath, String vaultUrl, String vaultToken, MorpheusContext morpheusContext) {
    return this._delete(vaultPath, vaultUrl, vaultToken, morpheusContext)
  }

  public ServiceResponse read(String vaultPath, String vaultUrl, String vaultToken, MorpheusContext morpheusContext) {
    ServiceResponse resp = this._read(vaultPath, vaultUrl, vaultToken, morpheusContext)
    if(resp.getSuccess()) {
      return ServiceResponse.success(resp.getData()?.data?.data as Map)
    } else {
      return resp
    }
  }

  public ServiceResponse save(String vaultPath, Map value, String vaultUrl, String vaultToken, MorpheusContext morpheusContext) {
    Map body = [data: value]
    return this._save(vaultPath, body, vaultUrl, vaultToken, morpheusContext)
  }
  
  @Override
  public String getFullVaultPath(String engineMount, String secretPathSuffix, String name, Map opts = [:]) {
    if(secretPathSuffix.endsWith('/')) {
      return engineMount + "/data/" + secretPathSuffix + name
    } else {
      return engineMount + "/data/" + secretPathSuffix + "/" + name
    }
  }
  
  public String getDescription() {
    return "KV Version 2 Vault Engine"
  }
  
  public String getName() {
    return "KV Version 2"
  }
  
  public String getDefaultEngineMount() {
    return "secret"
  }
  
}