#include <stdint.h>
#include "vscb.h"
/* Инициализация контекста шифра буфером с одноразовым ключом
 */
void
vscb_init(vscb_ctx_t *context, uint8_t *aKey, uint16_t keySize) {
    context->key = aKey ;
    context->key_size = keySize ;
    context->key_pos = 0 ;
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
 *               и зашифрованный буфер будет помещен на место открытого.
 */
int vscb_encrypt(vscb_ctx_t *context, void *clear_text, uint16_t text_size, void *encrypted) {
    uint8_t *e, *c ;
    uint16_t i ;

    if (0 == text_size) {
        return VSCB_SUCCESS;
    }
    c = clear_text ;
    if (NULL != encrypted) {
        e = encrypted ;
    } else { c=e;
    }
    for (i = 0; i < text_size; i++) {
        if (context->key_pos >= context->key_size) {
            return VSCB_SHORT_KEY ;
        }
        *e = (*c ^ context->key[context->key_pos]) ;
        context->key[context->key_pos] = 0 ;
        context->key_pos++ ;
        e++ ;
        c++ ;
    }
    return VSCB_SUCCESS ;
}
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
 *               и зашифрованный буфер будет помещен на место открытого.
 */
int vscb_decrypt(vscb_ctx_t *context, void *encrypted, uint16_t text_size, void *clear_text) {
    uint8_t *e, *c ;
    uint16_t i ;
    if (0 == text_size) {
        return VSCB_SUCCESS;
    }
    e = encrypted ;
    if (NULL != clear_text) {
        c = clear_text ;
    } else {
        c=e; }
    for (i = 0; i < text_size; i++) {
        if (context->key_pos >= context->key_size) {
            return VSCB_SHORT_KEY ;
        }
        *c = (*e ^ context->key[context->key_pos]) ;
        context->key[context->key_pos] = 0 ;
        context->key_pos++ ;
        e++ ;
        c++ ; }
    return VSCB_SUCCESS;
}