package com.registration.util.impl

import spock.lang.Specification
import spock.lang.Unroll

import static org.springframework.util.Base64Utils.encodeToString

@Unroll
class EmailDecoderSpec extends Specification {
    def decoder = [] as EmailDecoderImpl

    def "should decode email"() {
        expect:
        decoder.decode(code) == email

        where:
        email             | code
        'sins@jet.us'     | encodeToString("sins@jet.us:$_".bytes)
        'test@groovy.com' | encodeToString("test@groovy.com:$_".bytes)
        'jake@rambler.co' | encodeToString("jake@rambler.co:$_".bytes)
    }
}
