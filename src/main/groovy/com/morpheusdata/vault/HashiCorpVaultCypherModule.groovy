package com.morpheusdata.vault

import com.morpheusdata.cypher.Cypher
import com.morpheusdata.cypher.CypherModule
import com.morpheusdata.cypher.CypherObject
import com.morpheusdata.vault.util.*
import groovy.util.logging.Slf4j
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.response.ServiceResponse
import groovy.json.*

@Slf4j
class HashiCorpVaultCypherModule implements CypherModule {

  Cypher cypher
  MorpheusContext morpheusContext
  Plugin plugin
  
  public void setMorpheusContext(MorpheusContext morpheusContext) {
    this.morpheusContext = morpheusContext
  }
  
  public void setPlugin(Plugin plugin) {
    this.plugin = plugin
  }
  
  @Override
  public void setCypher(Cypher cypher) {
      this.cypher = cypher
  }
  
  @Override
  public CypherObject write(String relativeKey, String path, String value, Long leaseTimeout, String leaseObjectRef, String createdBy) {
    if(value != null && value.length() > 0) {
      String key = relativeKey
      String vaultPath = relativeKey
      Map body
      if(path != null) {
        key = path + "/" + key
      }
      if(relativeKey.startsWith("config/")) {
        return new CypherObject(key,value,0l, leaseObjectRef, createdBy)
      } else {
        String vaultUrl = this.getVaultUrl()
        String vaultToken = this.getVaultToken()
        
        AbstractVaultEngine vaultApiEngine = HashiCorpVaultPluginUtil.getVaultEngineFromPath(relativeKey)
        if (vaultApiEngine == null) {
          vaultApiEngine = HashiCorpVaultPluginUtil.getVaultEngine(HashiCorpVaultPluginUtil.DEFAULT_ENGINE_CODE)
        } else {
          vaultPath = HashiCorpVaultPluginUtil.getVaultPath(relativeKey) ?: relativeKey
        }
        
        try {
          body = new JsonSlurper().parseText(value)
        } catch (Exception ex) {
          log.error("Unable to decode JSON value getting saved into Cypher: {}", value)
          return null
        }
        
        ServiceResponse response = vaultApiEngine.save(vaultPath, body, vaultUrl, vaultToken)
        if (response.getSuccess()) {
          return new CypherObject(key,value,leaseTimeout, leaseObjectRef, createdBy)
        } else {
          return null
        }
      }
    } else {
        return null
    }
  }

  @Override
  public CypherObject read(String relativeKey, String path, Long leaseTimeout, String leaseObjectRef, String createdBy) {
    String key = relativeKey
    String value
    String vaultPath = relativeKey
    if(path != null) {
      key = path + "/" + key
    }
    if(relativeKey.startsWith("config/")) {
      return null
    } else {
      String vaultUrl = this.getVaultUrl()
      String vaultToken = this.getVaultToken()

      AbstractVaultEngine vaultApiEngine = HashiCorpVaultPluginUtil.getVaultEngineFromPath(relativeKey)
      if (vaultApiEngine == null) {
        vaultApiEngine = HashiCorpVaultPluginUtil.getVaultEngine(HashiCorpVaultPluginUtil.DEFAULT_ENGINE_CODE)
      } else {
        vaultPath = HashiCorpVaultPluginUtil.getVaultPath(relativeKey) ?: relativeKey
      }
      
      ServiceResponse response = vaultApiEngine.read(vaultPath, vaultUrl, vaultToken)
      
      if (response.getSuccess()) {
        value = new JsonBuilder(response.getData())?.toString()
      }
      
      try {
        CypherObject vaultResult = new CypherObject(key,value,leaseTimeout,leaseObjectRef, createdBy)
        vaultResult.shouldPersist = false
        return vaultResult

      } catch(Exception ex) {
        ex.printStackTrace()
        return null
      }
    }
  }

  @Override
  public boolean delete(String relativeKey, String path, CypherObject object) {
    if(relativeKey.startsWith("config/")) {
      return true
    } else {
      String vaultPath = relativeKey
      String vaultUrl = this.getVaultUrl()
      String vaultToken = this.getVaultToken()
      
      AbstractVaultEngine vaultApiEngine = HashiCorpVaultPluginUtil.getVaultEngineFromPath(relativeKey)
      if (vaultApiEngine == null) {
        vaultApiEngine = HashiCorpVaultPluginUtil.getVaultEngine(HashiCorpVaultPluginUtil.DEFAULT_ENGINE_CODE)
      } else {
        vaultPath = HashiCorpVaultPluginUtil.getVaultPath(relativeKey) ?: relativeKey
      }
      return vaultApiEngine.delete(vaultPath, vaultUrl, vaultToken)?.getSuccess()      
    }
  }

  @Override
  public String getUsage() {
    StringBuilder usage = new StringBuilder()
    usage.append("This allows secret data to be fetched to/from a HashiCorp Vault server. This can be configured in the Plugin settings.")
    return usage.toString()
  }

  @Override
  public String getHTMLUsage() {
    return null
  }
  
  private String getVaultUrl() {
    return HashiCorpVaultPluginUtil.getVaultUrl(this.morpheusContext, this.plugin, this.cypher)
  }
  
  private String getVaultToken() {
    return HashiCorpVaultPluginUtil.getVaultToken(this.morpheusContext, this.plugin, this.cypher)
  }

}
