## Official Morpheus HashiCorp Vault Plugin

This is the official Morpheus plugin for interacting with HashiCorp Vault. This plugin enables the ability to read & store secure Cypher entries & credentials for various clouds, tasks, and integrations remotely in a secure HashiCorp vault store external to Morpheus and Cypher. It is also intended to supersede the native Morpheus vault Cypher integration that currently exists within Morpheus Cypher Core, and currently provides support for both KV1 and KV2 HashiCorp Vault secret engines whereas the native Morpheus integration only supports KV2. Additional Vault Secret Engine support can be added to this Plugin by the community in the future. Credentials and cypher entries can be shared across multiple Morpheus appliances using the same vault store.

### Building

This is a Morpheus plugin that leverages the `morpheus-plugin-core` which can be referenced by visiting [https://developer.morpheusdata.com](https://developer.morpheusdata.com). It is a groovy plugin designed to be uploaded into a Morpheus environment via the `Administration -> Integrations -> Plugins` section. To build this product from scratch simply run the shadowJar gradle task on java 11:

```bash
./gradlew shadowJar
```

A jar will be produced in the `build/lib` folder that can be uploaded into a Morpheus environment.


### Configuring

#### Cypher

Please refer to https://docs.morpheusdata.com/en/latest/tools/cypher.html#vault in version 5.5.1 for details on the plugin and how it works with the native vault integration.

#### Credentials

Once the plugin is loaded in the environment. Vault Becomes available in `Infrastructure -> Trust -> Services`.

When adding the integration simply enter the URL of the Vault Server (no path is needed just the root url) and the token with sufficient enough privileges to talk to the secret Engine API. 
If a URL or Token is not provided in the credentials integration then they will be taken from the Plugin settings if available. 
After configuring the URL and Token choose a Secret Engine technology to use and optionally configure the Secret Engine Mountpoint and Secret Path Suffix.
If Secret Engine Mountpoint and Secret Path Suffix are left blank, then credential secrets using the integration will be saved into the default engine mountpoint and "morpheus-credentials" path.
For KV Version 1 and KV Version 2 engines the default engine mountpoint will be "secret".  

#### Further info
Further updates will be discussed on the community post here: https://discuss.morpheusdata.com/t/hashicorp-vault-cypher-plugin/416 and documentation included within the official Morpheus documentation.

### Contributing
Please feel free to extend/add-to this plugin via pull requests.


