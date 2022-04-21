# Reina
An Android mobile chat application developed in Java and Firebase.
BEYKOZ UNIVERSITY

Architecture and Engineering Faculty / Computer Engineering

ENGINEERING PROJECT III - Spring 2022

Project Scope Definition & Requirement List

**Project Name :**Reina Messenger

**Project Link :[Reina on Github](https://github.com/mertfozzy/Reina)**

**Project Summary :**“An Android mobile chat application developed in Java and Firebase.” **Project Developers :**Mert Altuntaş, Kaan Çeliktaş, Alperen Demirtürkoğlu


#1. Project Description

Reina Messenger is a mobile chat application that supports the Android operating system. It is designed to make it easier for people to text-based messages and share documents with each other on the mobile platform.

Users who want to use Reina have to create their own accounts. At this point, unique accounts can be opened with a personal phone number or e-mail address. In this way, users are instantly ready to chat on Reina with their accounts.

It is builded in Java programming language, also the database is managed with Google’s Firebase.

#2. Requirement List
1. Functional Requirements
1. End-User Functions

2.1.1.1.Viewing Profile Informations (Picture + Name + Status)

The user can access the profile information of other users in the contact list. This includes the user profile photo, name and online/offline information.

2.1.1.2.Personal / Private Chatting

The user can message one-on-one with other users in the contact list. This chat can only take place between two users, end-to-end.

2.1.1.3.Group Chatting

The user can message in existing chat groups or groups that he or she has created. Group messages can be made with more than 2 people. All users who are members of the group can access and reply to messages.

2.1.1.4.Message Request System

With the user message request, a message request can be sent to other users using the application who are not in the contact list. In this way, communication can be provided between different users.

2.1.1.5.Online/Offline Status

The user can find out if the other user they want to message is online or not. If it is offline, it can also see the last seen information.

2.1.1.6.Sending a PDF -or DOC

It is possible for users to send documents with pdf or doc extensions to each other. 2.1.1.7.Sending an Image

The user can send image files with the extension of jpeg, jpg or png.

2.1.1.8.Delete Message System

The user can permanently delete messages during the chat.

2. Operator Functions 2.1.2.1.Login, Signup to the Application

To access the application, users must login with their own account information. If users do not have an account, they must sign up then login again.

1. *Login & Signup Interface Design*

Reina developers will design a simple and understandable interface for users to login and signup.

2. *Firebase Authentication for Login*

One of the login methods is using email and password information. When the user signup, the information entered is registered in the Firebase database. It is checked for possible errors, and users will have an account on the system. Each user has to use a unique email address.

3. *Using Phone Number for Login*

Another login method is the phone number. The user can login to the system with the verification code sent to the phone number.

4. *Logout Functions*

The user can logout at any time.

2.1.2.2.Message and Chat Activity Functions

1. *Group Chat Activity*

Reina developers will develop a group chat feature so that groups can message each other.

2. *Sending and Receiving Message*

The correct design of the messaging system is the most important element for a chat application. Therefore, the developer team will carefully develop the sending-receiving features.

3. *Last Seen and Online Status*

Availability of users can be an important factor during chat. Therefore, access to last seen information will be provided.

4. *Message Request*

The "find friends" feature is a good feature to start a chat. However, messaging by people who do not know each other requires consent. For this reason, the "messaging request" system will be developed.

5. *Sending Image and Documents Features*

Users may want to send documents and images to each other when necessary. The application will have a document submission download system to support this as well.

6. *Deleting Messages*

The user will be able to delete messages permanently during the chat.

2.1.2.3.General Interface Functions

1. *View My Profile Section*

It should have a “profile settings” section where the user can update their profile information from within the application. The developers will design this partition as well.

2. ***Viewing Available Contacts***

The developers will also design a “contacts” section where the user can see the contact list and profile information of the users in the list.

3. ***Chat Window Transactions (Scrollview)***

As new messages arrive during the chat, the interface screen should slide up. Some visual gestures such as Scrollview will be integrated into the application to improve usage.

2. Nonfunctional Requirements
1. Connecting to a web app

The application can only be used with the mobile version on Android in its current form. A Web-accessible version may also be released in the future.

2. Lock and unlock with fingerprint

In order to increase security, a locking system can be made that allows users to access the application with a fingerprint.

3. Speech to text message sending

With dictation, a technology can be developed that transcribes speech sounds. In this way, text messages can be sent without the need to use a keyboard.

4. Adding stories section for users

Posting status with “story” has become a very popular social media habit. Instant and disappearing images can be shared by developing story modes.

5. End to end encryption

To increase security, access can be provided with an end-to-end encryption system during account login. So in case of loss/theft, the account still remains safe.

6. Sending stickers, GIFs

Stickers and GIFs expressing emotions during chat can strengthen the communication experience for users.

7. Video/Voice call

With video or audio calling features, communication can reach the next level. In this way, text-message disadvantages are overcome at more important moments.

#3.Potential Users

**Users of small groups:**

- ***A company staff:*** This app can be used to talk among the personnel working in a certain department of a company.
- ***A school society students:*** This app can be used to conduct communications within the society.
- ***Management of a site (building / neighbourhood):***This app can be used for some meetings of the site management and for decisions to be taken about the site.
- ***Friendship Relationships (BETA):*** Users who want to make friends can talk by adding each other.

\4. Commercialization Potential

In this project, there are 4 main topics that we will examine in the commercialization steps; advertising, finances, business processes and internationalisation. If we start with advertising first, research is required in order to advertise the product to the right audience and to the customer who has the need. After that, another important issue is to promote this product through both written and visual media by evaluating it according to cost calculations.

In addition, one of the most important elements in the field of advertising will be the user experience, so it should be advertised by adding the comments and opinions of other users who used this product. Secondly, financial situation calculations should be prepared in detail with professional service. Income and expenses that may arise should be examined, and certain conditions and provisions should be put into the contract to avoid additional costs that may cause loss afterwards.

Thirdly, the business process. What needs to be done here is the smooth execution of the titles that we mentioned earlier. By making rapid progress in the commercial process of the product with a successful cost calculation and advertising, we achieve success both in the name of a large customer pool and branding. Since our product is a message application, we must establish commercial connections with companies such as Google Play Store and IOS App Store for its use. In this way, we will contribute to our other topic, internationalisation.

Because these app stores are the most popular stores with worldwide users, they are very important for the product to spread in the global market. Here, our responsibility is to make different customizations by adding different language options according to the rate of customer usage of the product in the world. For example, different themes can be provided for different countries, in this way, a special application feeling can be provided to the user.
6
