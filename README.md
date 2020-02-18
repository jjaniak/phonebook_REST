## Spring MVC PhoneBook REST application
This is a learning project making part of Spring course. The project requirements can be found more at the bottom.

## Endpoints:

### List all contacts
`GET /api/v1/contacts`

Returns an array of all contacts

### Get phone numbers for contact
`GET /api/v1/contacts/:name`

Returns all phone numbers belonging to requested name (an array of Strings)

### Create new contact
`POST /api/v1/contacts`

Example request body:
```JSON
{
	"name": "Marylin",
	"phoneNumbers": [
		"+1853820479",
		"+1365993072"
	]
}
```
Returns just created Contact and HTTP status is: **201 Created**.

### Add phone number for contact
`PUT /api/v1/contacts/:name`

Example request body (can add only one phone number at once):
```JSON
["+198765432"]
```
Returns just updated Contact.


### Delete contact
`DELETE /api/v1/contacts/:name`

Deletes contact for requested name. It does not return anything.

## Errors and status codes
- If you try to get phone numbers for contact, add phone number for contact or delete a contact using a name that is not in the phone book, 
then expect **404** status code and error in the following format:

```JSON
{
    "status": "404",
    "message": "There is no contact with name 'xxx' in the phone book"
}
```

- If a request fails validation (incorrect request body/ body content etc.), 
expect a **400** status code and error either in the same format as above or in the following format:

```JSON
[
    {
        "field": "phoneNumbers",
        "message": "Contact need to contain at least one phone number"
    },
    {
        "field": "name",
        "message": "Contact need to have a name"
    }
]
```


## Project requirements
Take [PhoneBook application](https://github.com/jjaniak/phonebook) as an example  and create Spring MVC (REST) application as follow:

- GET - receive all phoneBook records
- GET/{name} - all phones for the given name
- PUT/{name}; phoneNumber - add phone to existing name
- POST/; {“name”:”YourName”, “phoneNumber”: “+79998887711”} - create a new record in the phoneBook
- DELETE/{name} - removes record by name completely (including associated phone numbers).
It must throw an exception if there is no such name in the PhoneBook. The exception must be handled and reason must be printed out as a JSON object.

#### Additional info:
- Rest resources must be located under **api/v1/contacts**
- Use JSON as request/response body.
- All records can be kept in memory.
- Ensure that appropriate HTTP status codes are returned for ALL methods.
- Java configuration of Spring MVC is a must.