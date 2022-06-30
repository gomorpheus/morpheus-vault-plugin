## Official Morpheus HashiCorp Vault Plugin

This is the official Morpheus plugin for interacting with HashiCorp Vault. This plugin enables the ability to read & store secure Cypher entries & credentials for various clouds, tasks, and integrations remotely in a secure HashiCorp vault store external to Morpheus and Cypher. It is also intended to supersede the native Morpheus vault Cypher integration that currently exists within Morpheus Cypher Core, and provides support for both KV1 and KV2 HashiCorp Vault secret engines whereas the native Morpheus integration only supports KV2. Credentials and cypher entries can be shared across multiple Morpheus appliances using the same vault store.

### Building

This is a Morpheus plugin that leverages the `morpheus-plugin-core` which can be referenced by visiting [https://developer.morpheusdata.com](https://developer.morpheusdata.com). It is a groovy plugin designed to be uploaded into a Morpheus environment via the `Administration -> Integrations -> Plugins` section. To build this product from scratch simply run the shadowJar gradle task on java 11:

```bash
./gradlew shadowJar
```

A jar will be produced in the `build/lib` folder that can be uploaded into a Morpheus environment.


### Configuring

#### Cypher

A Version 1.0.0 video demo of the Cypher plugin provider can be found here: https://d.pr/v/i5k5MS. **In version 1.2.0 and above use the “vault” mountpoint instead of “hashicorpVault”**

Further updates will be discussed on the community post here: https://discuss.morpheusdata.com/t/hashicorp-vault-cypher-plugin/416

#### Credentials

Once the plugin is loaded in the environment. Vault Becomes available in `Infrastructure -> Trust -> Services`.

When adding the integration simply enter the URL of the Vault Server (no path is needed just the root url) and the token with sufficient enough privileges to talk to the secrets kv API.

This is the official Morpheus plugin for interacting with HashiCorp Vault.  Currently this Plugin only implements a Cypher provider, however it can be extended to be a Credentials provider within the future.

### Contributing
Please feel free to extend/add-to this plugin via pull requests.


