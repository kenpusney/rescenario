package net.kimleo.rescenario.model

import io.restassured.http.Method

class RequestAction {
    Method method
    String path

    static RequestAction of(List<String> action, String method, String path) {
        if (method != null && path != null) {
            return of(method, path)
        } else {
            if (action == null) {
                throw new RuntimeException("Cannot translate request action: unknow action")
            } else {
                return of(action[0], action[1])
            }
        }
    }

    static RequestAction of(String method, String path) {
        return new RequestAction(method: Method.valueOf(method.toUpperCase()), path: path)
    }

    String toString() {
        return  "[$method $path]"
    }
}