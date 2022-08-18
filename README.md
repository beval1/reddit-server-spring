 <h2>Deployed application</h2>
 <p>The application is deployed on heroku: https://reddit-server-spring.herokuapp.com/</p>

  <h2>Brief desciption</h2>
 <p>This project is an attempt to recreate very basic copy of reddit backend service with monolith architecture. It is a REST API and its using JWT tokens to authorize users.</p>
 <p>Graphical user interface is being developed in this repo: https://github.com/beval1/reddit-clone-react</p>
 
 <h2>Technologies used</h2>
<ul>
 <li>Java</li>
 <li>Spring Boot</li>
 <li>PostgresSQL 14</li>
 <li>Docker</li>
 <li>Cloudinary (for storing images)</li>
 <li>Prometheus (for sraping data for analytics)</li>
 <li>Graphna (GUI for analyzing application performance)</li>
 <li>Swagger-UI (GUI for REST API documentation)</li>
</ul>
 
 <h2>Functionality</h2>
 <p>There are three types of users - normal user, moderator of subreddit and application admin</p>

 <p>Users can:</p>
        <ul>
            <li>create posts</li>
			<li>delete their own post</li>
            <li>join subreddits</li>
			<li>create subreddits</li>
            <li>upvote and downvote posts</li>
            <li>upvote and downvote comments</li>
            <li>edit their own profile</li>
            <li>add their own profile image</li>
            <li>add their own banner image</li>
            <li>edit their own comment</li>
            <li>delete their own comment</li>
        </ul>
       <p>Subreddit Moderator is just a normal user with moderator role in given subreddit, they can:</p>
        <ul>
            <li>delete user comment</li>
            <li>ban users from subreddit</li>
        </ul>
        <p>Application admins are users with global admin permission, they can:</p>
        <ul>
            <li>ban/disable user accounts</li>
            <li>delete user posts in EVERY subreddit</li>
            <li>delete user comments in EVERY subreddit</li>
            <li>update EVERY subreddit - changing main image, banner, description</li>
        </ul>
        
<h2>Test coverage</h2>
  <p>The application has a little bit over 70% lines coverage mainly from integration tests.</p>
  
  <h2>API</h2>
	![auth_controller](https://user-images.githubusercontent.com/86118441/185499258-3364afd9-2664-4c2b-ae91-a677c402e09c.png)
  ![comment_controller](https://user-images.githubusercontent.com/86118441/185499315-5123af04-9cd5-4b79-a4a6-a4b13625eae8.png)
  ![feed-controller](https://user-images.githubusercontent.com/86118441/185499334-62e27b3f-0c20-4499-be4c-39b614e8e04b.png)
  ![post-controller](https://user-images.githubusercontent.com/86118441/185499359-c0dca71f-3b35-458c-a63f-773563b4aab8.png)
  ![subreddit_controller](https://user-images.githubusercontent.com/86118441/185499386-b30c741e-535e-4148-a2c1-8608ab97187c.png)
  ![user_controller](https://user-images.githubusercontent.com/86118441/185499398-d9a60a2d-8d4a-45f2-8390-25c8ad5bc404.png)


  <h2>How to Run?</h2>
  <p>To run localy, you must set the enviroment variables in application.yaml file.</p>
  <p>You can also run the application with docker compose. There are two profiles - "local" and "full". 
  Profile "local" will start database, prometheus and graphana containers.
  Profile "full" will start all the containers "local" starts but it will also make container of the spring application. 
  Note: You must have packaged and placed the .jar file of the application in the docker directory to start "full" profile </p>
 

 </ul>
