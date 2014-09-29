Task List
=======

This is a for-fun project where I build a Java Server Application using Java 8.

This server exposes a REST API that allows for the creation of users and tasks associated with them. Tasks can be repeatable and the logic contained in the server will determine the next due date of repeatable tasks.

Authentication
------

This server uses basic authentication along with Sprint Security to separate tasks out by user. Basic authentication can be added in several ways.

1. Use a header containing the base64 encoded string of <username>:<password>
  * `Authorization: Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==`
  * where `QWxhZGRpbjpvcGVuIHNlc2FtZQ==` is the Base64 encoded credentials
2. Embed the credentials in the server URL
  * `https://user:passwd@www.server.com/index.html`
  * These credentials (and the URL) are encrypted over SSL


API
------

The REST API exposes the following endpoints.

<hr />

### Create Account (/user/create)

Creates a new account on the server. This is the only unauthenticated API call.

**Request (POST,PUT) (Form URL Encoded)**
* username
* password

**Resposne (JSON)**

* User Entity
  * username (string)
  * password (string)
  * id (long)

<hr />

### Get All Tasks (/task/all)

Get all tasks (including completed tasks) associated with the authenticated user.

**Request (GET)**
  * No Parameters
  
**Response (JSON)**
  * List of Tasks
    * id (long)
    * title (string)
    * description (string)
    * group (string)
    * dueDate (`yyyy-MM-dd'T'HH:mm:ss`)
    * completeDate (`yyyy-MM-dd'T'HH:mm:ss`)
    * isActive (boolean)
    * repeatable (string, enum)
      * NONE
      * DAILY
      * WEEKLY
      * MONTHLY
      * YEARLY

<hr />

### Get Active Tasks (/task/active)

Get tasks that have not been completed or are repeatable.

**Request (GET)**
  * No Parameters

**Response (JSON)**
  * List of Tasks

<hr />

### Create New Task (/task/add)

Add a new task

**Request (POST, JSON body)**
  * Task

**Response (JSON)**
  * Task

<hr />

### Complete Task (/task/complete)

Mark a task as complete. If the task is repeatable, the due date is moved forward based on the repeatable time increment.

**Request (POST) (Form URL Encoded)**
  * taskId (long)
  * date (`yyyy-MM-dd'T'HH:mm:ss`)
    * date when the task was completed

**Respsonse (NONE)**
  * 200 OK if completed successfully.'

<hr />

### Delete Task (/task/delete)

Permanently remove the task.

**Request (POST) (Form URL Encoded)**
  * taskId (long)

**Response (NONE)**
  *200 OK if the task is deleted
  
<hr />

### Edit Task (/task/edit)

Edit/Update a task.

**Request (PUT,POST) (JSON body)**
  * task

**Resposne (NONE)**
  * 200 OK if task updated successfully

License
------

Licensed under the MIT License
