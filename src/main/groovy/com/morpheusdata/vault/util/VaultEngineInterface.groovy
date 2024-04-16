package com.morpheusdata.vault.util
import com.morpheusdata.response.ServiceResponse
import com.morpheusdata.core.MorpheusContext

public interface VaultEngineInterface {
  
  public ServiceResponse delete(String vaultPath, String vaultUrl, String vaultToken, MorpheusContext morpheusContext)

  public ServiceResponse read(String vaultPath, String vaultUrl, String vaultToken, MorpheusContext morpheusContext)

  public ServiceResponse save(String vaultPath, Map value, String vaultUrl, String vaultToken, MorpheusContext morpheusContext)

  public String getDescription()

  public String getName()

  public String getDefaultEngineMount()

  public String getCode()

}