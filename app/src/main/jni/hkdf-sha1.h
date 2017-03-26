//
// Created by Andrew Chupin on 25.03.17.
//

#ifndef NATIVE_HKDF_SHA1_H
#define NATIVE_HKDF_SHA1_H

#include <stdint.h>

int hkdf_sha1(
        const unsigned char *salt, int salt_len,
        const unsigned char *ikm, int ikm_len,
        const unsigned char *info, int info_len,
        uint8_t okm[ ], int okm_len) ;
int hkdf_sha1Extract(
        const unsigned char *salt, int salt_len,
        const unsigned char *ikm, int ikm_len,
        uint8_t prk[SHA1HashSize]) ;
int hkdf_sha1Expand(const uint8_t prk[ ], int prk_len,
                    const unsigned char *info, int info_len,
                    uint8_t okm[ ], int okm_len) ;
#endif //NATIVE_HKDF_SHA1_H
