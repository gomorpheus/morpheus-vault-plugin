package com.morpheusdata.vault

import com.morpheusdata.core.Plugin
import com.morpheusdata.model.Permission
import com.morpheusdata.model.OptionType
import com.morpheusdata.vault.util.HashiCorpVaultPluginUtil

/**
 * 
 * @author Chris Taylor
 */
class HashiCorpVaultPlugin extends Plugin {

	@Override
	String getCode() {
		return 'morpheus-hashicorp-vault-plugin'
	}

	@Override
	void initialize() {
		HashiCorpVaultOptionSourceProvider optionSourceProvider = new HashiCorpVaultOptionSourceProvider(this, morpheus)

		this.pluginProviders.put("hashicorp-vault-credentials", new HashiCorpVaultCredentialProvider(this, morpheus))
		this.pluginProviders.put("hashicorp-vault-cypher", new HashiCorpVaultCypherProvider(this,morpheus))
		this.pluginProviders.put(optionSourceProvider.code, optionSourceProvider)
		this.setName("HashiCorp Vault")
		this.setDescription("HashiCorp Vault Plugin")
		this.setAuthor("Morpheus")
		this.setSourceCodeLocationUrl("https://github.com/gomorpheus/morpheus-vault-plugin")
		this.setIssueTrackerUrl("https://github.com/gomorpheus/morpheus-vault-plugin/issues")
		
		this.settings << new OptionType (
			name: 'HashiCorp Vault Url',
			code: 'hashicorp-vault-plugin-url',
			fieldName: 'hashicorpVaultPluginUrl',
			displayOrder: 0,
			fieldLabel: 'HashiCorp Vault Url',
			helpText: 'The full URL of the HashiCorp Vault Server. For example: http://example.vault.server:8200',
			required: false,
			inputType: OptionType.InputType.TEXT
		)
		
		this.settings << new OptionType (
			name: 'HashiCorp Vault API Token',
			code: 'hashicorp-vault-plugin-api-token',
			fieldName: 'hashicorpVaultPluginToken',
			displayOrder: 1,
			fieldLabel: 'HashiCorp Vault Token',
			helpText: 'The HashiCorp Vault Token',
			required: false,
			inputType: OptionType.InputType.PASSWORD
		)
		
		this.settings << new OptionType (
			name: 'Supported Engines',
			code: 'hashicorp-vault-plugin-supported-engines',
			fieldName: 'hashicorpVaultPluginSupportedEngines',
			displayOrder: 2,
			fieldLabel: 'Supported Engines',
			helpText: 'A list of HashiCorp Vault secret engines that this Plugin version supports',
			required: false,
			inputType: OptionType.InputType.TEXTAREA,
			editable: false,
			defaultValue: this.getSupportedEngines()
		)
		
	}

	@Override
	void onDestroy() {
		//nothing to do for now
	}
	
	private getSupportedEngines() {
		def rtn = ""
		HashiCorpVaultPluginUtil.SUPPORTED_ENGINES?.each{ k, v -> rtn += v.getName() + " Engine\n" }
		return rtn
	}
	
}
