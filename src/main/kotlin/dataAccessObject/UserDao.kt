package dataAccessObject

import model.User
import java.util.concurrent.atomic.AtomicInteger

class UserDao {
    private val users = hashMapOf(
        0 to User(id = 0, name = "Alice", email = "alice@alice.kt"),
        1 to User(id = 1, name = "Bob", email = "bob@bob.kt"),
        2 to User(id = 2, name = "Carol", email = "carol@carol.kt"),
        3 to User(id = 3, name = "Dave", email = "dave@dave.kt")
    )

    private val lastId: AtomicInteger = AtomicInteger(lastId())

    fun usersList(): Map<Int, User> = users

    fun save(name: String, email: String): User {
        val id = lastId.incrementAndGet()
        val user = User(id = id, name = name, email = email)
        users[id] = user

        return user
    }

    fun update(id: Int, name: String, email: String): User {
        val user = User(id = id, name = name, email = email)
        users[id] = user

        return user
    }

    fun delete(id: Int) {
        users.remove(id)
    }

    fun findBy(id: Int): User? = users[id]

    fun findBy(email: String): User? {
        return users.values.find { user -> user.email == email }
    }

    private fun lastId() = users.keys.maxOrNull() ?: 0
}