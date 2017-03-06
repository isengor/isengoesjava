using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TcpServer2._0
{
    public class MyDB
    {

        private SqlConnection connection;
        private SqlCommand addMsg;

        public MyDB()
        {
            
            connection = new SqlConnection("Data Source=(local);Initial Catalog=msghistory;"
                                          + "Integrated Security=true");
           
            
        }

        public void addMsgToDB(String msg, String ipAdress)
        {
            connection.Open();

            addMsg = new SqlCommand("INSERT INTO msg_table(name,date,msg,ip) VALUES(@name,@date,@msg,@ip)", connection);

            addMsg.Parameters.AddWithValue("@name", getSenderName(msg));
            addMsg.Parameters.AddWithValue("@date", DateTime.Now.ToString());
            addMsg.Parameters.AddWithValue("@msg", msg.Replace(getSenderName(msg)+": ",""));
            addMsg.Parameters.AddWithValue("@ip", ipAdress);

            addMsg.ExecuteNonQuery();

            connection.Close();
        }

        public String getSenderName(String msg)
        {
            String name="";

            char[] foo = msg.ToCharArray();

            foreach (var s in foo)
            {
                if (s == ':') return name;

                else name += s;
            }

            return name;
        }        

    }
}
