package kennethzeng.example.flickrbrowser

import java.io.IOException
import java.io.ObjectStreamException
import java.io.Serializable

data class Photo(var title: String, var author: String,
                 var authorId: String, var link: String,
                 var tags: String, var image: String): Serializable {
    companion object {
        private const val serialVersionUID = 1L
    }

    @Throws(IOException::class)
    private fun writeObject(out: java.io.ObjectOutputStream) {
        out.writeUTF(title)
        out.writeUTF(author)
        out.writeUTF(authorId)
        out.writeUTF(link)
        out.writeUTF(tags)
        out.writeUTF(image)
    }

    @Throws(IOException::class, ClassNotFoundException::class)
    private fun readObject(inStream: java.io.ObjectInputStream) {
        title = inStream.readUTF()
        author = inStream.readUTF()
        authorId = inStream.readUTF()
        link = inStream.readUTF()
        tags = inStream.readUTF()
        image = inStream.readUTF()
    }

    @Throws(ObjectStreamException::class)
    private fun readObjectNoData() {

    }
}