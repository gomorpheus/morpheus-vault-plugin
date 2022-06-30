package com.morpheusdata.vault

import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.model.*
import groovy.util.logging.Slf4j
import com.morpheusdata.core.OptionSourceProvider
import com.morpheusdata.vault.util.*

@Slf4j
class HashiCorpVaultOptionSourceProvider implements OptionSourceProvider {

	Plugin plugin
	MorpheusContext morpheusContext

	HashiCorpVaultOptionSourceProvider(Plugin plugin, MorpheusContext context) {
		this.plugin = plugin
		this.morpheusContext = context
	}

	@Override
	MorpheusContext getMorpheus() {
		return this.morpheusContext
	}

	@Override
	Plugin getPlugin() {
		return this.plugin
	}

	@Override
	String getCode() {
		return 'hashicorp-vault-option-source-plugin'
	}

	@Override
	String getName() {
		return 'HashiCorp Vault Option Source Plugin'
	}

	@Override
	List<String> getMethodNames() {
		return new ArrayList<String>(['engines'])
	}

	def engines(args) {
    def engines = []
    HashiCorpVaultPluginUtil.SUPPORTED_ENGINES?.each{ k, v -> engines << [value: v.getCode(), name: v.getName()] }
    return engines
	}
}