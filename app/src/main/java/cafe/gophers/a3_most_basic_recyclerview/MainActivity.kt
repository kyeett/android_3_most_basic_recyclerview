package cafe.gophers.a3_most_basic_recyclerview

import android.Manifest
import android.content.ContentResolver
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val data = listOf<String>("Magnus", "Wahlstrand", "Adam","Steve", "John", "Corolla", "Smithy")


        find_friends_button.setOnClickListener {
            find_friends_button.visibility = View.GONE

            val p = loadContacts()

            viewAdapter = ImageRectAdapter(p.first.toList(), p.second)

            recyclerView = findViewById<RecyclerView>(R.id.recycler_view).apply {
                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                setHasFixedSize(true)

                // specify an viewAdapter (see also next example)
                adapter = viewAdapter

            }

            recyclerView.visibility = View.VISIBLE
        }
    }


    private val REQUEST_CODE_ASK_PERMISSIONS = 126

    private fun loadContacts() : Pair<Set<String>, Int> {

        val hasWriteContactsPermission = checkSelfPermission(Manifest.permission.READ_CONTACTS)
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_CONTACTS),
                REQUEST_CODE_ASK_PERMISSIONS
            )
            TODO("SOMETHING WENT WRONG!!")
        }

        return getContacts()
    }

    private fun getContacts(): Pair<Set<String>, Int>{

        val dbList = listOf<String>("234", "456", "678")
        val matching = ArrayList<String>()
        val notMatching = ArrayList<String>()

        val resolver: ContentResolver = contentResolver;
        val cursor = resolver.query(
            ContactsContract.Contacts.CONTENT_URI, null, null, null,
            null)


        if (cursor != null && cursor.count > 0) {
            while (cursor.moveToNext()) {
                val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val phoneNumber = (cursor.getString(
                    cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))).toInt()
                val rawID = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.NAME_RAW_CONTACT_ID))


                if (phoneNumber > 0) {
                    val cursorPhone = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", arrayOf(id), null)

                    if(cursorPhone != null && cursorPhone.count > 0) {
                        while (cursorPhone.moveToNext()) {
                            val phoneNumValue = cursorPhone.getString(
                                cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                            if (phoneNumValue in dbList) {
                                Log.e("Magnus", "We have a match ${phoneNumValue} for ${id}, ${rawID}, ${name}")
                                matching.add(name)
                                continue
                            }

//                            builder.append("Contact: ").append(name).append(", Phone Number: ").append(
//                                phoneNumValue).append("\n\n")

//                            Log.e("Other ==>", builder.append("Contact: ").append(name).append(", Phone Number: ").append(
//                                phoneNumValue).append(", ID: ").append(
//                                id).append(", Raw ID: ").append(
//                                rawID).append("\n\n").toString())
//                            Log.e("Name ===>",phoneNumValue);
                        }
                    }
                    cursorPhone?.close()
                }

                // If we have come this far, add to the not matching list
                notMatching.add(name)

            }
        } else {
            //   toast("No contacts available!")
        }
        cursor?.close()


        val all= ArrayList<String>()
        all.addAll(matching.toSet())
        all.addAll(notMatching.toSet().reversed())

        return Pair(all.toSet(), matching.toSet().size)
    }

}
