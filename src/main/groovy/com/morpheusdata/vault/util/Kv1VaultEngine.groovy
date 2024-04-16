package com.morpheusdata.vault.util
import com.morpheusdata.response.ServiceResponse
import com.morpheusdata.core.MorpheusContext

class Kv1VaultEngine extends AbstractVaultEngine {
  
  Kv1VaultEngine(String code) {super(code)}
  
  public ServiceResponse delete(String vaultPath, String vaultUrl, String vaultToken, MorpheusContext morpheusContext) {
    return this._delete(vaultPath, vaultUrl, vaultToken, morpheusContext)
  }

  public ServiceResponse read(String vaultPath, String vaultUrl, String vaultToken, MorpheusContext morpheusContext) {
    ServiceResponse resp = this._read(vaultPath, vaultUrl, vaultToken, morpheusContext)
    if(resp.getSuccess()) {
      return ServiceResponse.success(resp.getData()?.data as Map)
    } else {
      return resp
    }
  }

  public ServiceResponse save(String vaultPath, Map value, String vaultUrl, String vaultToken, MorpheusContext morpheusContext) {
    return this._save(vaultPath, value, vaultUrl, vaultToken, morpheusContext)
  }
  
  public String getDescription() {
    return "KV Version 1 Vault Engine"
  }
  
  public String getName() {
    return "KV Version 1"
  }
  
  public String getDefaultEngineMount() {
    return "secret"
  }
  
}