# CS/COE 1501 Assignment 5
Goal:
To get hands on experience with the use of digital signatures.

Note that the result of this project should NEVER be used for any security applications. It is purely instructive. Always use trusted and tested crypto libraries.

High-level description:
You will be writing two programs. The first will generate a 512-bit RSA keypair and store the public and private keys in files named pubkey.rsa and privkey.rsa, respectively. The second will generate and verify digital signatures using a SHA-256 hash. You will use Java's MessageDigest class to complete this project.

Specifications:
Write a program named RsaKeyGen to generate a new RSA keypair.
To generate a keypair, follow the following steps, as described in lecture.
Pick p and q to be random primes of an appropriate size to generate a 512-bit key
Calculate n as p*q
Calculate φ(n) as (p-1)*(q-1)
Choose an e such that 1 < e < φ(n) and gcd(e, φ(n)) = 1 (e must not share a factor with φ(n))
Determine d such that d = e⁻¹ mod φ(n)
After generating e, d, and n, save e and n to pubkey.rsa, and d and n to privkey.rsa.
Write a second program named RsaSign to sign files and verify signatures. This program should accept two command-line arguments: a flag to specify whether to sign or verify (s or v), and the name of the file to sign/verify.
If called to sign (e.g., java RsaSign s myfile.txt) your program should:
Generate a SHA-256 hash of the contents of the specified file (e.g., myfile.txt).
"Decrypt" this hash value using the private key stored in privkey.rsa (i.e., raise the hash value to the dth power mod n).
Your program should exit and display an error if privkey.rsa is not found in the current directory.
Write out the signature to a file named as the original, with an extra .sig extension (e.g., myfile.txt.sig).
If called to verify (e.g., java RsaSign v myfile.txt) your program should:
Read the contents of the original file (e.g., myfile.txt).
Generate a SHA-256 hash of the contents of the original file.
Read the signed hash of the original file from the corresponding .sig file (e.g., myfile.txt.sig).
Your program should exit and display an error if the .sig file is not found in the current directory.
"encrypt" this value with the key from pubkey.rsa (i.e., raise it to the eth power mod n).
Your program should exit and display an error if pubkey.rsa is not found in the current directory.
Compare the hash value that was generated from myfile.txt to the one that was recovered from the signature. Print a message to the console indicating whether the signature is valid (i.e., whether the values are the same).
Submission Guidelines:
DO NOT upload any IDE package files.
You must name your key generation program RsaKeyGen.java, and your signing/verification program RsaSign.java.
You must be able to compile your program by running javac RsaKeyGen.java and javac RsaSign.java.
You must be able to run your key generation program by running java RsaKeyGen, and your signing/verification program with java RsaSign s <filename> and java RsaSign v <filename>.
You must fill out info_sheet.txt.
The project is due at 11:59 PM on Friday, April 21. Upload your progress to Box frequently, even far in advance of this deadline. No late assignments will be accepted. At the deadline, your Box folder will automatically be changed to read-only, and no more changes will be accepted. Whatever is present in your Box folder at that time will be considered your submission for this assignment—no other submissions will be considered.
Additional Notes and Hints:
You may not use the Java API class java.math.BigInteger, or any other JCL class to represent large integers. Instead, you should store integers in memory as raw integers represented using byte arrays (i.e., instances of byte[]).

Being limited to byte arrays will require you to write a number of methods for manipulating these "raw integers", including those to:

add two raw integers,
multiply two raw integers,
determine whether two raw integers are equal,
and so on. Even simple operations, including those for which you are used to having operators, will need to be implemented specially for this representation of integer.

You do not need to write code to generate large random prime integers, as we have provided RandomPrime.java, which contains a method generate(int n, java.util.Random rnd). This method generates an n-bit random positive integer using rnd as a source of randomness, and uses a probabilistic primality test to ensure that it is probably prime (with 2^-100 chance of being composite). This method returns a byte[] representation of the integer.

The representation returned by this method is in big-endian byte-order, so the most significant byte is at the 0th index of the byte array. It also ensures that there is at least one leading 0 bit (indicating that the integer is positive using a two's-complement representation). This last property may cause the array to be bigger than expected: a 1024-bit prime is returned as a length 129 byte array.
You may want to create a simple wrapper class (e.g., MyBigInteger.java) that stores the absolute value of the integer as a byte array and the sign as a boolean, rather than use two's-complement encoding in your byte[].

An example of using java.security.MessageDigest to generate the SHA-256 hash of a file is provided in HashEx.java

You may find the creation of pubkey.rsa, privkey.rsa, and signature files to be most easily accomplished through the use of java.io.ObjectOutputStream. The format of your key and signature files is up to you.

NEVER USE CODE FROM THIS PROJECT IN PRODUCTION CODE. This is purely instructive. Always use trusted and tested crypto libraries.
