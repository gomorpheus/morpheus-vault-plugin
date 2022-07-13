package com.morpheusdata.vault

import com.morpheusdata.core.CypherModuleProvider
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.cypher.CypherModule

class HashiCorpVaultCypherProvider implements CypherModuleProvider{
  MorpheusContext morpheusContext
  Plugin plugin

  HashiCorpVaultCypherProvider(Plugin plugin, MorpheusContext morpheusContext) {
    this.plugin = plugin
    this.morpheusContext = morpheusContext
  }

  @Override
  CypherModule getCypherModule() {
    HashiCorpVaultCypherModule module = new HashiCorpVaultCypherModule()
    module.setMorpheusContext(this.morpheusContext)
    module.setPlugin(this.plugin)
    return module
  }

  @Override
  String getCypherMountPoint() {
    return 'vault'
  }

  @Override
  MorpheusContext getMorpheus() {
    return morpheusContext
  }

  @Override
  String getCode() {
    return 'hashicorp-vault-cypher'
  }

  @Override
  String getName() {
    return 'Hashicorp Vault Cypher'
  }
}
