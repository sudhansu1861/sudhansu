package org.grails.twitter
import org.grails.twitter.Person

class StatusController {
def springSecurityService
    def index() {
		
		
		def messages=currentUserTimeline()
		[messages: messages]
		
	}
	def updateStatus(String message){
    def status=new Status(message: message)
	
	status.author=lookupPerson()
	status.save(flush: true, failOnError: true)
    def messages=currentUserTimeline()
	//render messages
	render template: 'messages',collection: messages, var: 'message'
	
}
	private currentUserTimeline(){
		def per=lookupPerson()
		System.out.println("Person name is "+per.username);
		/*def messages=Status.withCriteria{
			author {
				eq 'username',per.username
                System.out.println(per.username);
			}
		
			maxResults 10
			order 'dateCreated','desc'
	}*/
		
		//messages
		def per1 = Person.findByUsername(per.username)
		def query = Status.whereAny {
			author { username == per1.username }
			
		}.order 'dateCreated', 'desc'
		def messages = query.list(max: 10)
		//System.out.println(messages)
		messages

	}
	private lookupPerson(){
		
		Person.get(springSecurityService.principal.id)
		
	}
	
	def follow(long personId) {
		def person = Person.get(personId)
		if (person) {
			def currentUser = lookupPerson()
			currentUser.addToFollowed(person)
			timelineService.clearTimelineCacheForUser(currentUser.username)
			
		}

				//statusService.follow id
		redirect action: 'index'
	}

}