using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using System.Collections;

namespace TcpServer2._0
{
    class Program
    {
               
        public static TcpListener server = new TcpListener(5001);
        public static String data = null;
        public static List<NetworkStream> streamList = new List<NetworkStream>();
        public static MyDB myDB = new MyDB();
       
        
        class MyThread
        {
            private Thread thread;
            private Byte[] bytes = new Byte[256];
          
            private TcpClient client;


            public MyThread(TcpClient client)
            {
                this.client = client;
                thread = new Thread(this.func);
                thread.Start();
                
            }

            void func()
            {                
                                      
                        
                        int i;
                        NetworkStream stream = client.GetStream();
                        String clientIp = client.Client.RemoteEndPoint.ToString();

                        Console.WriteLine(clientIp + " connected!");
                        streamList.Add(stream);
                        try
                        {
                            while ((i = stream.Read(bytes, 0, bytes.Length)) != 0)
                            {

                                //handmade fast converter byteToChar, unfortunatly doesnt work with utf 8 :(

                                //for (int s = 0; s <= i; s++)
                                //{
                                //    data += (char)bytes[s];
                                //}

                                data = ASCIIEncoding.UTF8.GetString(bytes,0,i);
                              
                                Console.WriteLine(clientIp + ": " + data);

                                try
                                {
                                    myDB.addMsgToDB(data, clientIp);
                                }
                                catch (Exception ex)
                                {
                                    Console.WriteLine(ex.Message);
                                }

                                // as an echo server, when msg received we sending it back to all the connected clients,
                                // including msg sender
                                
                                foreach (var s in streamList)
                                {
                                    try
                                    {
                                        s.Write(bytes, 0, bytes.Length);
                                    }
                                    catch
                                    {

                                    }
                                }


                                Array.Clear(bytes, 0, bytes.Length);
                                data = null;

                            }
                        }

                        catch (Exception ex)
                        {
                            Console.WriteLine("IOException, broken connection:" + ex.Message);
                        }

                        finally
                        {
                        Console.WriteLine(client.Client.RemoteEndPoint.ToString() + " disconnected!");
                        Thread.CurrentThread.Abort();
                    
                        }
            }

        }

        static void Main(string[] args)
        {
            server.Start();


            //listenning socket, in case of pending a new thread creates

            while (true)
            {
                if (server.Pending()) { new MyThread(server.AcceptTcpClient()); }
            }
            
        }

        
    }
}
