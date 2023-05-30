#!/bin/bash

. ./json_base.sh


rest_api_echo() {
    local msg
    msg=$(JSON_PARAM_STRING "$1" '.msg')

    API_RESPONSE_RETURN true 1 "no error" "$( JSON_BUILDER msg  "$msg")"
}