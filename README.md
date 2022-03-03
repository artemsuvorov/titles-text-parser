# Titles Text Parser

Titles Text Parser is a RESTful API service developed using Spring Boot. 
The Application java class runs a server which you can send HTTP request to.
Mainly, user can upload a file (in CR, LF or CRLF format) and get its parsed representation 
in any of several formats: plain, parsed or json (more on that in the Usage section).

## Quick start

Start by cloning the repository with:
```console
$ git clone https://github.com/artemsuvorov/titles-text-parser.git
```

Then compile all the .java files from the src directory and run the server.

By default, the server launches on localhost:8080

## Usage

The service provides the user with several number of HTTP requests:

| Request                             | Description                                                                                          |
|:------------------------------------|:-----------------------------------------------------------------------------------------------------|
| `GET /`                             | Get index.html page                                                                                  |
| `POST /upload`                      | Upload a specified file; returns home page with status message and links to file in several formats. |
| `GET /files/{filename}`             | Get text of a specified file in plain format.                                                        |
| `GET /files/parsed/{filename}`      | Get text of a specified file in html format.                                                         |
| `GET /files/parsed/json/{filename}` | Get text of a specified file in json format.                                                         |

Firsty, you need to upload a text file to the server via `POST /upload` request. 
CR, LF and CRLF text files are supported.
After uploading, you can access it in several formats via the corresponding `GET` 
request from the table above.

The uploaded file can be represented in a few formats:
1. plain: returns text in html format where text is splitted into paragraphs of html tag `<p>`
2. parsed: return text in html format where text is splitted into headers (`<h*>`) and paragraphs (`<p>`); on the top of the html page the navigation bar is provided.
3. json: returns text in json format where text is splitted into objects which represent headers or other lines of the text.

You can find examples of those formats in the Example section.

## Example

As it was mentioned before, there are several formats of the resulting text: plain, parsed, 
and json. You can choose the format of the parsed text by specifying it in your GET request.

For example, this piece of input text:

```md
# Header 1
Hello, world!
## Subheader 1
# Header 2
```

will be parsed into this html (simplified version to serve an example):

```html
<h1>Header 1</h1>
    <p>Hello, world!</p>
    <h2>Subheader 1</h2>
<h1>Header 2</h1>
```

or into this json:

```json
{
  "lines": [
    { "text": "Header 1", "isHeader": true, "level": 1 },
    { "text": "Hello, world!", "isHeader": false },
    { "text": "Subheader 1", "isHeader": true, "level": 2 },
    { "text": "Header 2", "isHeader": true, "level": 1 },
  ]
}
```