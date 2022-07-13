package com.morpheusdata.vault.util
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.cypher.Cypher
import com.morpheusdata.core.Plugin
import groovy.json.*

class HashiCorpVaultPluginUtil {
  
  private static final String KV1_ENGINE_CODE = "KV1"
  private static final String KV2_ENGINE_CODE = "KV2"
  
  public static final String DEFAULT_ENGINE_CODE = KV2_ENGINE_CODE//legacy as the native Morpheus module only supported KV2
  public static final HashMap<String, AbstractVaultEngine> SUPPORTED_ENGINES = new HashMap<String, AbstractVaultEngine>(){{
      put(KV1_ENGINE_CODE, new Kv1VaultEngine(KV1_ENGINE_CODE))
      put(KV2_ENGINE_CODE, new Kv2VaultEngine(KV2_ENGINE_CODE))
  }}
  
  public static AbstractVaultEngine getVaultEngine(String code) {
    return SUPPORTED_ENGINES.get(code)
  }
  
  public static String getVaultUrl(MorpheusContext morpheusContext, Plugin plugin, Cypher cypher = null) {
    def rtn
    def settings = getSettings(morpheusContext, plugin)
    if (settings.hashicorpVaultPluginUrl) {
      rtn = settings.hashicorpVaultPluginUrl
    } else {
      if (cypher) {
        rtn = cypher.read("vault/config/url")?.value
      }
    }
    return rtn
  }

  public static String getVaultToken(MorpheusContext morpheusContext, Plugin plugin, Cypher cypher = null) {
    def rtn
    def settings = getSettings(morpheusContext, plugin)
    if (settings.hashicorpVaultPluginUrl) {
      rtn = settings.hashicorpVaultPluginToken
    } else {
      if (cypher) {
        rtn = cypher.read("vault/config/token")?.value
      }
    }
    return rtn
  }
  
  public static String getVaultPath(String relativePath) {
    String rtn = null
    if (relativePath != null) {
      String[] splitPath = relativePath.split('/', 2)
      if (splitPath.length > 1) {
        rtn = splitPath[1]
      }
    }
    return rtn
  }

  public static AbstractVaultEngine getVaultEngineFromPath(String relativePath) {
    AbstractVaultEngine rtn = null
    if (relativePath != null) {
      String[] splitPath = relativePath.split('/')
      if (splitPath.length > 0) {
        String engine = splitPath[0].toUpperCase()
        rtn = SUPPORTED_ENGINES.get(engine)
      }
    }
    return rtn
  }

  private static getSettings(MorpheusContext morpheusContext, Plugin plugin) {
    def settings = morpheusContext.getSettings(plugin)
    def settingsOutput = ""
    settings.subscribe(
      { outData -> 
        settingsOutput = outData
      },
      { error ->
        println error.printStackTrace()
      }
    )

    JsonSlurper slurper = new JsonSlurper()
    def settingsJson = slurper.parseText(settingsOutput)
    return settingsJson
  }

}