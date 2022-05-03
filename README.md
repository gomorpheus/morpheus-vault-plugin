## Vault Credentiapl Plugin

This is the official Morpheus plugin for interacting with Hashicorp Vault. This plugin enables the ability to store secure credentials for various clouds, tasks, and integrations remotely in a secure store external to Morpheus and Cypher. This utilizes Vault's Secrets K-V V2 API and can be configured to store the credentials in sub paths if necessary for more than one morpheus appliance to share the same vault store.

### Building

This is a Morpheus plugin that leverages the `morpheus-plugin-core` which can be referenced by visiting [https://developer.morpheusdata.com](https://developer.morpheusdata.com). It is a groovy plugin designed to be uploaded into a Morpheus environment via the `Administration -> Integrations -> Plugins` section. To build this product from scratch simply run the shadowJar gradle task on java 11:

```bash
./gradlew shadowJar
```

A jar will be produced in the `build/lib` folder that can be uploaded into a Morpheus environment.


### Configuring

Once the plugin is loaded in the environment. Vault Becomes available in `Infrastructure -> Trust -> Services`.

When adding the integration simply enter the URL of the Vault Server (no path is needed just the root url) and the token with sufficient enough privileges to talk to the secrets kv API.

