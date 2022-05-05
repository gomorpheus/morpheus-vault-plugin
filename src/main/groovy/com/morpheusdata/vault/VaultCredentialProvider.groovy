package com.morpheusdata.vault

import com.morpheusdata.core.CredentialProvider
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.util.ConnectionUtils
import com.morpheusdata.core.util.HttpApiClient
import com.morpheusdata.model.AccountCredential
import com.morpheusdata.model.AccountIntegration
import com.morpheusdata.model.Icon
import com.morpheusdata.model.OptionType
import com.morpheusdata.response.ServiceResponse
import groovy.util.logging.Slf4j

@Slf4j
class VaultCredentialProvider implements CredentialProvider {
    MorpheusContext morpheusContext
    Plugin plugin

    VaultCredentialProvider(Plugin plugin, MorpheusContext morpheusContext) {
        this.morpheusContext = morpheusContext
        this.plugin = plugin
    }

    /**
     * Periodically called to test the status of the credential provider.
     * @param integration the referenced integration object to be loaded
     */
    @Override
    void refresh(AccountIntegration integration) {
        //NOTHING TODO FOR NOW
    }

    /**
     * Used to load credential information on the fly from the datastore. The data map should be the credential data to be loaded on the fly
     * @param integration the referenced integration object to be loaded
     * @param credential the credential reference to be loaded.
     * @param opts any custom options such as proxySettings if necessary (future use)
     * @return
     */
    @Override
    ServiceResponse<Map> loadCredentialData(AccountIntegration integration, AccountCredential credential, Map opts) {
        String secretPathSuffix = integration.getConfigProperty("secretPath") ?: "morpheus-credentials/"
        HttpApiClient apiClient = new HttpApiClient()
        try {
            //we gotta fetch from Vault
            if(!secretPathSuffix.endsWith('/')) {
                secretPathSuffix = secretPathSuffix + '/'
            }f
            String vaultPath="/v1/secret/data/" + secretPathSuffix + formatApiName(credential.name)
            def apiResults = apiClient.callJsonApi(integration.serviceUrl,vaultPath,new HttpApiClient.RequestOptions(headers: ['X-VAULT-TOKEN': integration.serviceToken]),'GET')
            log.info("Received Dataset: ${apiResults.data.data?.data}")
            if(apiResults.success) {
                ServiceResponse<Map> response = new ServiceResponse<>(true,null,null,apiResults.data.data?.data as Map)
                return response
            } else {
                return ServiceResponse.error(apiResults.error)
            }
        } finally {
            apiClient.shutdownClient()
        }
    }

    /**
     * Deletes the credential on the remote integration.
     * @param integration the referenced integration object containing information necessary to connect to the endpoint
     * @param credential the credential to be deleted
     * @param opts any custom options such as proxySettings if necessary (future use)
     * @return
     */
    @Override
    ServiceResponse<AccountCredential> deleteCredential(AccountIntegration integration, AccountCredential credential, Map opts) {
        String secretPathSuffix = integration.getConfigProperty("secretPath") ?: "morpheus-credentials/"
        HttpApiClient apiClient = new HttpApiClient()
        try {
            //we gotta fetch from Vault
            if(!secretPathSuffix.endsWith('/')) {
                secretPathSuffix = secretPathSuffix + '/'
            }
            String vaultPath="/v1/secret/data/" + secretPathSuffix + formatApiName(credential.name)
            def apiResults = apiClient.callJsonApi(integration.serviceUrl,vaultPath,new HttpApiClient.RequestOptions(headers: ['X-VAULT-TOKEN': integration.serviceToken]),'DELETE')
            if(apiResults.success) {
                ServiceResponse<AccountCredential> response = new ServiceResponse<>(true,null,null,credential)
                return response
            } else {
                return ServiceResponse.error(apiResults.error,null,credential)
            }
        } finally {
            apiClient.shutdownClient()
        }
    }

    /**
     * Creates the credential on the remote integration.
     * @param integration the referenced integration object containing information necessary to connect to the endpoint
     * @param credential the credential to be created
     * @param opts any custom options such as proxySettings if necessary (future use)
     * @return
     */
    @Override
    ServiceResponse<AccountCredential> createCredential(AccountIntegration integration, AccountCredential credential, Map opts) {
        String secretPathSuffix = integration.getConfigProperty("secretPath") ?: "morpheus-credentials/"
        HttpApiClient apiClient = new HttpApiClient()
        try {
            //we gotta fetch from Vault
            if(!secretPathSuffix.endsWith('/')) {
                secretPathSuffix = secretPathSuffix + '/'
            }
            String vaultPath="/v1/secret/data/" + secretPathSuffix + formatApiName(credential.name)
            def apiResults = apiClient.callJsonApi(integration.serviceUrl,vaultPath,new HttpApiClient.RequestOptions(headers: ['X-VAULT-TOKEN': integration.serviceToken], body: [data: credential.data]),'POST')
            if(apiResults.success) {
                ServiceResponse<AccountCredential> response = new ServiceResponse<>(true,null,null,credential)
                return response
            } else {
                return ServiceResponse.error(apiResults.error,null,credential)
            }
        } finally {
            apiClient.shutdownClient()
        }
    }

    /**
     * Updates the credential on the remote integration.
     * @param integration the referenced integration object containing information necessary to connect to the endpoint
     * @param credential the credential to be updated
     * @param opts any custom options such as proxySettings if necessary (future use)
     * @return
     */
    @Override
    ServiceResponse<AccountCredential> updateCredential(AccountIntegration integration, AccountCredential credential, Map opts) {
        String secretPathSuffix = integration.getConfigProperty("secretPath") ?: "morpheus-credentials/"
        HttpApiClient apiClient = new HttpApiClient()
        try {
            //we gotta fetch from Vault
            if(!secretPathSuffix.endsWith('/')) {
                secretPathSuffix = secretPathSuffix + '/'
            }
            String vaultPath="/v1/secret/data/" + secretPathSuffix + formatApiName(credential.name)
            def apiResults = apiClient.callJsonApi(integration.serviceUrl,vaultPath,new HttpApiClient.RequestOptions(headers: ['X-VAULT-TOKEN': integration.serviceToken], body: [data: credential.data]),'PUT')
            if(apiResults.success) {
                ServiceResponse<AccountCredential> response = new ServiceResponse<>(true,null,null,credential)
                return response
            } else {
                return ServiceResponse.error(apiResults.error,null,credential)
            }
        } finally {
            apiClient.shutdownClient()
        }
    }

    /**
     * Validation Method used to validate all inputs applied to the integration of an Credential Provider upon save.
     * If an input fails validation or authentication information cannot be verified, Error messages should be returned
     * via a {@link ServiceResponse} object where the key on the error is the field name and the value is the error message.
     * If the error is a generic authentication error or unknown error, a standard message can also be sent back in the response.
     *
     * @param integration The Integration Object contains all the saved information regarding configuration of the Credential Provider.
     * @param opts any custom payload submission options may exist here
     * @return A response is returned depending on if the inputs are valid or not.
     */
    @Override
    ServiceResponse<Map> verify(AccountIntegration integration, Map opts) {

        return ServiceResponse.success()
    }

    /**
     * Provide custom configuration options when creating a new {@link AccountIntegration}
     * @return a List of OptionType
     */
    @Override
    List<OptionType> getIntegrationOptionTypes() {
        return [
                new OptionType(code: 'vault.serviceUrl', name: 'Service URL', inputType: OptionType.InputType.TEXT, fieldName: 'serviceUrl', fieldLabel: 'API Url', fieldContext: 'domain', displayOrder: 0),
                new OptionType(code: 'vault.serviceToken', name: 'Service Token', inputType: OptionType.InputType.PASSWORD, fieldName: 'serviceToken', fieldLabel: 'Token', fieldContext: 'domain', displayOrder: 1),
                new OptionType(code: 'vault.secretPath', name: 'Secret Path', inputType: OptionType.InputType.TEXT,defaultValue: 'morpheus-credentials/', fieldName: 'secretPath', fieldLabel: 'Secret Path', fieldContext: 'config', displayOrder: 2)
        ]
    }

    /**
     * Returns the Credential Integration logo for display when a user needs to view or add this integration
     * @since 0.12.3
     * @return Icon representation of assets stored in the src/assets of the project.
     */
    @Override
    Icon getIcon() {
        return new Icon(path:"hashicorpvault-black.svg", darkPath: "hashicorpvault-white.svg")
    }

    /**
     * Returns the Morpheus Context for interacting with data stored in the Main Morpheus Application
     *
     * @return an implementation of the MorpheusContext for running Future based rxJava queries
     */
    @Override
    MorpheusContext getMorpheus() {
        return morpheusContext
    }

    /**
     * Returns the instance of the Plugin class that this provider is loaded from
     * @return Plugin class contains references to other providers
     */
    @Override
    Plugin getPlugin() {
        return plugin
    }

    /**
     * A unique shortcode used for referencing the provided provider. Make sure this is going to be unique as any data
     * that is seeded or generated related to this provider will reference it by this code.
     * @return short code string that should be unique across all other plugin implementations.
     */
    @Override
    String getCode() {
        return "vault"
    }

    /**
     * Provides the provider name for reference when adding to the Morpheus Orchestrator
     * NOTE: This may be useful to set as an i18n key for UI reference and localization support.
     *
     * @return either an English name of a Provider or an i18n based key that can be scanned for in a properties file.
     */
    @Override
    String getName() {
        return "Vault"
    }

    static protected formatApiName(String name) {
        String rtn = name
        if(rtn) {
            rtn = rtn.replace(' - ', '-')
            rtn = rtn.replace(' ', '-')
            rtn = rtn.replace('/', '-')
            rtn = rtn.toLowerCase()
        }
        return URLEncoder.encode(rtn)
    }
}