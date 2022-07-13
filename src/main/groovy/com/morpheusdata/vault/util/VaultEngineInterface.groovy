package com.morpheusdata.vault.util
import com.morpheusdata.response.ServiceResponse

public interface VaultEngineInterface {
  
  public ServiceResponse delete(String vaultPath, String vaultUrl, String vaultToken)

  public ServiceResponse read(String vaultPath, String vaultUrl, String vaultToken)

  public ServiceResponse save(String vaultPath, Map value, String vaultUrl, String vaultToken)

  public String getDescription()

  public String getName()

  public String getDefaultEngineMount()

  public String getCode()

}