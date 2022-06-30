package com.morpheusdata.vault.util
import com.morpheusdata.response.ServiceResponse

class Kv2VaultEngine extends AbstractVaultEngine {

  Kv2VaultEngine(String code) {super(code)}

  public ServiceResponse delete(String vaultPath, String vaultUrl, String vaultToken) {
    return this._delete(vaultPath, vaultUrl, vaultToken)
  }

  public ServiceResponse read(String vaultPath, String vaultUrl, String vaultToken) {
    ServiceResponse resp = this._read(vaultPath, vaultUrl, vaultToken)
    if(resp.getSuccess()) {
      return ServiceResponse.success(resp.getData()?.data?.data as Map)
    } else {
      return resp
    }
  }

  public ServiceResponse save(String vaultPath, Map value, String vaultUrl, String vaultToken) {
    Map body = [data: value]
    return this._save(vaultPath, body, vaultUrl, vaultToken)
  }
  
  public String getDescription() {
    return "KV Version 2 Vault Engine"
  }
  
  public String getName() {
    return "KV Version 2"
  }
  
  public String getDefaultSecretMount() {
    return "secret"
  }
  
}