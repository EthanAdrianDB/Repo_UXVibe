# Carpeta Wallet de Oracle

Coloca aquí todos los archivos extraídos del archivo `Wallet_XXXX.zip` de tu base de datos Oracle Autonomous Database:

- `cwallet.sso`
- `ewallet.p12`
- `ewallet.pem`
- `keystore.jks`
- `ojdbc.properties`
- `sqlnet.ora`
- `tnsnames.ora`
- `truststore.jks`

> **Nota:** Por motivos de seguridad, los archivos de certificados (`*.sso`, `*.p12`, `*.jks`, `*.ora`, etc.) son ignorados automáticamente por `.gitignore` para no exponer las llaves privadas en el repositorio.
