import dataAccessObject.UserDao
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import model.Error
import model.User

fun main(args: Array<String>) {
    val userDao = UserDao()
    val app = Javalin.create().apply {
        exception(Exception::class.java) { exception, _context -> exception.printStackTrace() }
        error(404) { context -> context.json(Error(code = "not_found", message = "Resource not found")) }
    }.start(7000)

    app.routes {
        get("/users") { context -> context.json(userDao.usersList())}

        get("/users/:user-id") { context ->
            context.json(userDao.findBy(context.pathParam("user-id").toInt())!!)
        }

        get("users/email/:email") { context ->
            context.json(userDao.findBy(context.pathParam("email"))!!)
        }

        post("/users") { context ->
            val userData = context.body<User>()
            val user = userDao.save(name = userData.name, email = userData.email)
            context.json(user)
            context.status(201)
        }

        put("users/:id") { context ->
            val userData = context.body<User>()
            val user = userDao.update(id = context.pathParam("id").toInt()!!, name = userData.name, email = userData.email)
            context.json(user)
            context.status(200)
        }

        delete("users/:id") { context ->
            val user = userDao.findBy(context.pathParam("id").toInt()!!)
            user?.let {
                context.json(it)
                userDao.delete(it.id)
                context.status(200)
                it
            } ?: context.status(404)
        }
    }
}