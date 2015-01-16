package taglib.org.grails.twitter

class StatusTagLib {
    static namespace = 'twitter'
    
    def renderMessages = { attrs ->
        def messages = attrs.messages
        messages.eachWithIndex { message, counter ->
            out << g.render(template: '/status/messages', model: [message: message, messageCounter: counter])
        }
    }
}
