Developing Team & Credits:
-HongCheng Wei, YuHao Yang, yi chen liu, Yu Sun, Yue Lan, Zhexuan Li, Qiyue Zhang and Peiwen Luo

>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

*IMPORTANT DETAIL*:

-You might need to add JUnit4 as dependencies for successfully run the program.

-When you run the program, you can find the login option on the first conference control page, input 0 and the
username, password of the admin and then you can operate the whole system then.

-The javadoc is stored at "phase1/javadoc", find the index.html and open to see the javadoc.

-UML class diagram is stored at "phase1/design", the UML is divided by packages.

-After the program finnish, the session is deserialized and stored at "phase1/session/Session.ser".

-If the program failed to find the session (may be due to can't find the path), the program would load a sample session
 which contains:
 The example session is loaded with the following account and some
 Talk to save you time when trying out features:
    - Two Organizer
       - Username : org1
         Password : 1
       - Username : org2
         Password : 2
    - Three Speaker
       - Username : spk1
         Password : 1
       - Username : spk2
         Password : 2
       - Username : spk3
         Password : 3
    - Four Attendee
       - Username : att1
         Password : 1
       - Username : att2
         Password : 2
       - Username : att3
         Password : 3
       - Username : att4
         Password : 4
    - Some Events,
       - Talk 1
         - Organizer : org1
         - Speaker : not set,
         - Location : Convocation Hall
         - Date : 2020/11/22
         - Time : 10:00 ~ 11:00
       - Talk 2
         - Organizer : org1
         - Speaker : not set,
         - Location : Bahen Center
         - Date : 2020/11/22
         - Time : 13:00 ~ 14:00
       - Talk 3
         - Organizer : org1
         - Speaker : not set,
         - Location : Convocation Hall
         - Date : 2020/11/22
         - Time : 13:00 ~ 14:00
       Notice that the Speaker of Talk 1 and Talk 2 can't be the same,
       because they have the same time and different location.

 Or alternatively you could load a empty session with just one organizer account
 (Username : org1, Password : 1)  by  setting the variable "loadExample = false"
 at the very beginning of the main method (line 26 at "ConferenceSystem.java").

>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

About:

-The program is basically developed to figure out series of problems and situations based on event management.

-In the project many tools were used to have an overview of timetables for various events, and to allow people
including Organizers, Speakers, and Attendees at a conference to communicate with each other.

>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

Features:

Schedule Algorithm

-The program has been settled down the algorithm to avoid schedule conflict to run the program smoothly.

---------------

UUID(universally unique identifier)

-UUID is one the special designing in this program. A universally unique identifier (UUID)
is a 128-bit number used to identify information in computer systems. The UUID is used everywhere in program
to record different information to keep and track them in a correct and organized way.

-The more information on UUID technology can check:
https://en.wikipedia.org/wiki/Universally_unique_identifier#:~:text=A%20universally%20unique%20identifier%20
(UUID,in%20software%20created%20by%20Microsoft.

---------------

Text_UI

-A whole, completed, organized, easy-operating Text-UI to operate the whole system including all the functionalities
and operations of the program.

---------------

Organizer

-Can organize the whole system of the program, the organizer account has authority to visit, browse and operate
all instructions of the system.
-An organizer account has following authorities and legal instructions such as Event Center, People Center, Friends
& Co-Workers, Messaging, Logoutand etc. The more detailed information can be
checked inside program.
-All upper operations and instructions should be implemented by organizers' permissions.

---------------

Speaker

-Can check the location, time and more details of his/her upcoming events.

---------------

Attendee

-Can create attendee account and attendee account can login the system by his/her own username and password.
The attendee can browse events list, view his/her own appointments, view his/her own friend list and messaging to

---------------

Schedule

-The schedule adequately addresses the issues of misleading People at the conference to attend any
event at the wrong time or to enter the wrong rooms. Attendees may follow the schedule to find out
the specific times and the specific rooms when and where Speakers give their talks.

---------------

Sign-Up System

-The Sign-Up System makes it possible for Attendees to browse the talks and sign up to attend the specific
talks that they are interested in. If an event is fully booked, Attendees will not be able to sign up for it.

---------------

Messaging System

-Speakers are able to send messages to all Attendees of their talk or multiple talks that they enter.
They can also respond to an Attendee who sent them individual messages.

-Organizers are able to send messages to all Speakers, one specific Speaker, all Attendees, or one specific Attendee.

-Attendees are able to message other Attendees or Speakers. They can also receive response from the specific Speaker
that they sent messages to.

>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

Operation illustration

Event Center(for organizer)

-For an organizer account, when you login into it. Then the organizer can create or change an event, it includes
create a talk and assign speaker for a specific event. For the future, the program might extend more kinds of events by
demands.

-For organizer to create a new event, it needs to give the event title, location and time. The allowed event setting
for now is from 9:00 to 18:00, and the system will not allow one speaker or attendee to join two event in same time by
algorithm.

---------------

People Center(for organizer)

-For an organizer account, when you login into it. Then the organizer can have following instructions such as
Create Organizer Account, Create Speaker Account, Message all Speaker (Direct Chat), Message all Attendee (Direct Chat).
To create a new organizer account or speaker account, it needs username and password to initiate the new account.
After initiating the new organizer account or speaker account, they would have whole functionalities and authorities to
operate the new account.

-Message all Speaker (Direct Chat), Message all Attendee (Direct Chat) are two tools for organizer to direct message to
all speakers and attendees, these functions do not allow the organizers, speakers and attendees are mutually friends.
This is for fast communication by organizer to send out messages quickly and conveniently.

---------------

Friends & Co-Workers(for organizer, attendee and speaker)

-An organizer has two kinds of relationships which are co-workers and friends, co-workers are connections with other
organizers and speakers and friends are connections with other attendees.

-To message, it needs to choose who you want to talk and what message you want to send. After sending message to
someone, you can check your message records on a chat board. The chat board clearly shows the time this message sent
the person name who sent this message and the content message.

-For attendee, a attendee can add friend in Add Friend tool, it will show a list of attendee they are able to add into
his/her own friend list, also any attendee can remove the existed friend from his/her own friend list. Only friends in
this system can send message to each other.

*****
  To finish to messaging, there are two ways,
  1. press enter without any message in chat line can quit messaging.
  2. input !exit and enter can quit messaging.
*****

---------------

Event Listing(for attendee)

-Event listing has following instruction signup and cancel signup. Attendee can choose the event they want to signup
from existing event list, the system algorithm will not be allowed to add two event have time conflict, otherwise the
conflict will not be added into attendee's event list.

-To cancel an event, it will report "You haven't signed up yet!" if the event you want to cancel you are not registered
in, if you successfully cancel the event, the system will report "Cancel Successful!" to show you have been removed the
event successfully.

---------------

View Appointment(for attendee)

-View Appointment is tool for attendee to check his/her own event appointment now, the events in View Appointment List
will not have time conflict events by algorithm, by matching View Appointment, the attendee can choose to signup more
events or canceling some specific events in Event Listing tool.

---------------

View Speaker Duties(for speakers)

-View Speaker Duties is one special tool for speakers to check their existed appointments, and speaker can choose to
whether cancel some appointments(duties) in this tool.

>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

Instruction exhibition

>>>Organizers<<<

log in

Create speaker accounts
    People Center -> Create Speaker Account

Create organiser accounts
    People Center -> Create Organiser Account

Create talk
    Event Center -> Event Create/Change -> Create Talk

Schedule the speakers
    Event Center -> Event Create/Change -> Assign Speaker

Message for organizer
  - To all speaker:
        People Center -> Message All Speaker -> add message

  - To all attendee:
        People Center -> Message All Attendee -> add message

  - To a specific speaker
        Friends & Co-worker	Message To -> Select the Speaker from Co-workers -> add message

  - To a specific attendee
        Friends & Co-worker	Message To -> Select the attendee from Co-workers -> add message

  - If you already chatted with a specific speaker or attendee:
        Messaging -> Select the Chat -> add message

---------------

>>>Speakers<<<

log in

To see a list of talks that they are giving, select the action
    View Speaker Duties

To message Attendees who are signed up for a particular event.
There are a few ways to accomplish this:
  - Message all attendees at once through Announcement, this way all signed up attendees
    can view the message (but they can't replay):
        Messaging -> Select the Announcement Group for the event -> add message

  - Message all attendees at once through Direct Chat, this way the message goes to a
    direct chat between the speaker and the attendee (the attendee can replay):
        Event Center -> Event Manage -> Message All Attendee -> Select the Event -> add message

  - Message all attendee Individually:
        Friends & Co-workers -> Message To -> Select the Attendee -> add message

  - If you already chatted with the attendee:
        Messaging -> Select the Chat to the Attendee -> add message

---------------

>>>Attendees<<<

log in

See a schedule of events for which they can sign up:
    Event Listing

Sign up for events:
    Event Listing -> Signup

Cancel their enrolment in an event:
    Event Listing -> Cancel Signup

See the schedule of the events for which they signed up:
    View Appointment

Send messages to and receive messages from other Attendees,
First you would need to add other attendees as friends:
  - Add Friends:
        Friends & Co-worker -> Add Friend

  - Remove Friends:
        Friends & Co-worker -> Remove Friend
S
  - Message other attendees:
        Friends & Co-worker -> Message To -> Select a Friends -> add message

  - If you already chatted with some attendee:
        Messaging -> Select the Chat with the Attendee -> add message

  - Message with Speakers
        Friends & Co-worker -> Message to -> Select a Co-worker	add message

>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

To Run the program
    src -> controller -> session -> ConferenceSystem.java -> run the main method
Remember to set the source root as the src folder.

Saving and Persistence of Information:
    Logout -> Exit Program
The program will serialize the session and store it in "phase1/session/Session.ser"

>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

Contribution

The technology acknowledgement from UTSG CS Professors and TAs
All contributions and credits are from team_0238.

Please read the wiki for more details. For the contributors with programming skills the headers are
filled with comments.

>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

Support and discussion

Support by group_0238
Discussed in English and Mandarin

>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

Thanks for reading.
Best Appreciate from all team members in Group_0238.