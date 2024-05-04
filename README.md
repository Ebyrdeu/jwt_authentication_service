## Generating Private and Public Keys Using OpenSSL

### Steps

#### 1. Generating the Base RSA Key
First, generate a base RSA private key. RSA keys are a pair of public and private keys that are used for secure data encryption.

**Command:**
```bash
openssl genrsa -out baseKey.pem 2048
```
- `genrsa`: OpenSSL command to generate RSA keys.
- `2048`: Specifies the key length in bits. Commonly used lengths are 2048 and 4096 bits for increased security.
- `baseKey.pem`: The filename for the generated private key.

#### 2. Converting the Base Key to PKCS#8 Format
The PKCS#8 format is used to store private keys. Convert the base key to this format using the following command:

**Command:**
```bash
openssl pkcs8 -topk8 -inform PEM -in baseKey.pem -out privateKey.pem -nocrypt
```
- `pkcs8`: OpenSSL command to manipulate PKCS#8 format private keys.
- `-topk8`: Indicates the conversion of a private key from traditional format to PKCS#8 format.
- `-inform PEM`: Specifies the input format. PEM (Privacy Enhanced Mail) is a common format for storing cryptographic keys.
- `-in baseKey.pem`: Specifies the input file which is the base key generated in step 1.
- `-out privateKey.pem`: Specifies the output file name for the PKCS#8 formatted private key.
- `-nocrypt`: Indicates that the output private key should not be encrypted.

#### 3. Generating the Public Key
Once you have the private key, you can generate its corresponding public key. The public key is used to encrypt data that can only be decrypted by the corresponding private key.

**Command:**
```bash
openssl rsa -in privateKey.pem -pubout -outform PEM -out publicKey.pem
```
- `rsa`: OpenSSL command to manage RSA keys.
- `-in privateKey.pem`: Specifies the input file which is the PKCS#8 formatted private key.
- `-pubout`: Indicates that the output should be a public key.
- `-outform PEM`: Specifies the output format.
- `-out publicKey.pem`: The filename for the generated public key.


### Verifying the Key Pair

After generating your private and public keys, it is important to verify that the public key corresponds to the private key. This ensures that the two can work together for encryption and decryption processes.

#### 4. Comparing the Public and Private Keys
You can verify that a public key matches a private key by comparing their cryptographic fingerprints or modulus values. The modulus value is a part of the key that is common to both the private and the public key.

**Command to Extract the Modulus from the Private Key:**
```bash
openssl rsa -in privateKey.pem -noout -modulus | openssl md5
```
- `rsa`: OpenSSL command to manage RSA keys.
- `-in privateKey.pem`: Specifies the input file which is the private key.
- `-noout`: Suppresses output other than the specified value.
- `-modulus`: Outputs the modulus of the key.
- `| openssl md5`: Pipes the output through an MD5 hash to simplify comparison.

**Command to Extract the Modulus from the Public Key:**
```bash
openssl rsa -pubin -in publicKey.pem -noout -modulus | openssl md5
```
- `-pubin`: Specifies that the input is a public key.
- `-in publicKey.pem`: Specifies the input file which is the public key.

**Compare the Outputs:**
After running both commands, you will receive an MD5 hash of the modulus for each key. If the two hashes are identical, it confirms that the public key matches the private key.
