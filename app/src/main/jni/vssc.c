#include <stdint.h>
#include "vssc.h"
#include "lcsx-rand.h"
/* Инициализация контекста потокового шифра двумя 32битными числами
 * K1, K2.
 */
void
vssc_init(vssc_ctx_t *context, uint32_t k1, uint32_t k2) {
    random_lcsx_init(&context->random_context, k1, k2) ;
}
/* Шифрование
 *
 * Функция шифрует содержимое открытого буфера и помещает его в
 * буфер зашифрованной информации.
 *
 * Параметры:
 * *context -- указатель на контекст потокового шифра
 * *clear_text -- указатель на открытый буфер
 * text_size -- длина буфера
 * *encrypted -- указатель на буфер размещения зашифрованной информации
 *               если NULL, то будет осуществляться шифрование "на месте"
* и зашифрованный буфер будет помещен на место открытого. */
void
vssc_encrypt(
        vssc_ctx_t *context,
        void *clear_text,
        uint32_t text_size,
        void *encrypted) {
    uint8_t *e, *c, r ;
    uint32_t i ;
    if (0 == text_size) {
        return; }
    c = clear_text ;
    if (NULL != encrypted) {
        e = encrypted ;
    } else { c=e;
    }
    for (i = 0; i < text_size; i++) {
        r = random_lcsx_v8(&context->random_context) ;
        *e = (*c ^ r) ;
        e++ ;
        c++ ;
    } }


/* Дешифрование
 *
 * Функция дешифрует содержимое закрытого буфера и помещает его в
 * открытый буфер.
 *
 * Параметры:
 * *context -- указатель на контекст потокового шифра
 * *encrypted -- указатель на буфер размещения зашифрованной информации
 * text_size -- длина буфера
 * *clear_text -- указатель на открытый буфер
 *               если NULL, то будет осуществляться шифрование "на месте"
* и зашифрованный буфер будет помещен на место открытого. */
void
vssc_decrypt(vssc_ctx_t *context, void *encrypted, uint32_t text_size, void *clear_text) {
    uint8_t *e, *c, r ;
    uint32_t i ;
    if (0 == text_size) {
        return; }
    e = encrypted ;
    if (NULL != clear_text) {
        c = clear_text ;
    } else {
        c=e; }
    for (i = 0; i < text_size; i++) {
        r = random_lcsx_v8(&context->random_context) ;
        *e = (*c ^ r) ;
        e++ ;
        c++ ;
    }
}