#!/bin/bash

if git describe --abbrev=0 --tags --exact-match HEAD 2> /dev/null; then
    echo "::set-env name=tag_present::true"
else
    echo "::set-env name=tag_present::false"
fi