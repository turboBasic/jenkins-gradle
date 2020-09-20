
void publish() {
    echo 'Publish document id = ' + """${
        (new groovy.json.JsonSlurperClassic()
            .parseText('''{
                "aaa": 593985
            }''') \
            as Map
        ).aaa
    }"""
}
