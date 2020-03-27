# Document v0.0.4

* TOC
{:toc}

## Get started

**File: Scenario.yml**
```yaml
services:
  - name: Example API
    domain: ["example", "default"]
    scheme: http
    host: localhost
    port: 8080
    endpoint: /
    # or simply url: https://localhost:8080/

scenarios:
  - name: Test Some Endpoint
    domain: example
    path:  /hello.json
    method:  GET
    headers:
      Accept: application/json
    expect:
      status: 200
      body:
        "message" : hello world!
```

Run `java -jar rescenario.jar Scenario.yml`, and you'll see the result.

## Reference

### Definitions

A Res file contains multiple definitions: `requires`, `services` and `scenarios`.

#### `requires` definition

Requires contains a list of dependency files, e.g.:

**File: Scenario.yml**
```yaml
requires:
  - Services.yml
  - Setup.yml
```

Rescenario assures that all scenarios in dependencies executed before
current file, and executed in declared order.

This means you can combine several scenario definitions into one by
creating a file and requires them. e.g.:

**File: ScenarioSuite.yml**
```yaml
requires:
  - RegistrationScenario.yml
  - CheckoutScenario.yml
  - PaymentScenario.yml
  - ...
```

#### `services` definition

Service definition is to create a RESTful service for each REST scenario.

```yaml
service:
  - name:       # required - the name of the service
    domain:     # required - the domains this service belongs to
    scheme:     # optional if `url` provided
    host:       # optional if `url` provided
    port:       # optional
    endpoint:   # optional if `url` provided
    url:        # optional: same as <scheme>://<host>:<port>/<endpoint>
```

Services are identified by **domain**, which will also be declared in
`scenario` definitions, Scenarios pick up services by filtering them
with **domain**.

#### `scenarios` definition

Scenario definition is to describe the steps to actual run the test.

```yaml
scenario:
  - name: Step 1
    type: ... # default: `rest`
    ....
  - name: Step 2
    type: ...
    ...
```

Scenario have several types, you can see details later.

### Scenario types

#### `rest` scenario

`rest` is default scenario type, which is a step to call a rest service.

```yaml
  - name: Call some endpoint
    # type: rest (by default)
    domain: some_domain
    path: /path/to/the/resource
    method: GET
    headers:
      Some-Http-Header: and its value
    body: >
      {
        "json": "..."
      }
    expect:
      status: 200
      body:
        result: $"success"
```

#### `exec` scenario

`exec` is to execute external command, you should provide the
actual `command` to execute and it's command line arguments(`args`).

```yaml
  - name: Execute a command
    type: exec
    command: echo
    args:
      - hello
      - world
```

#### `eval` scenario

`eval` is to calculate a groovy expression(`expr`) and `store` the result to
a variable.

```yaml
  - name: Evaluate an expr
    type: eval
    expr: 1 + 1
    store: two
```

#### `show` scenario

`show` is to display the `variable` content in storage.

```yaml
  - name: Show a variable
    type: show
    variable: two
```

#### `script` scenario

TODO

### The storage

Res uses a layered storage system, for each dependent, 
they can access and override the variable in it's dependency,
but cannot modify them. i.e. when the dependency were called
again, they still retrieve's original dependency's value.

You can access the storage when it's available. i.e. in following cases:

To extract a value:
  - `rest` scenario URL, using `${var_name}` format.
  - `expect` section, raw string treats as a variable name.
  - `eval` scenario, using `it['var_name']` to retrieve variable.
  - `show` scenario, in `variable` section, to retrieve variable.

To store a value:
  - `rest` scenario, using `store` section.
  - `eval` scenario, using `store` section.
  