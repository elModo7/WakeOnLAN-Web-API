![enter image description here](https://media.licdn.com/dms/image/v2/D4E12AQGgAUYFTIrFZw/article-cover_image-shrink_720_1280/article-cover_image-shrink_720_1280/0/1726585268186?e=1732147200&v=beta&t=eev1lBpfWriMsXW84kuLGA1OvKxz3ahOdWi-kcRs-8Q)
# Wake on LAN on Steroids for your HomeLab
![enter image description here](https://media.licdn.com/dms/image/v2/D4E12AQF6AW53XmHJKQ/article-inline_image-shrink_1500_2232/article-inline_image-shrink_1500_2232/0/1726568056394?e=1732147200&v=beta&t=ewBC8EpUBnNUM3v8IaNtCi9PfXQsgsXlUNNOoI1CrhI)

This tool will allow you to deploy your own **Web Panel + REST API** for managing your **devices's power status**.
You can expose it using _port-forwarding, VPN_ or _tunneling_ to allow for **Wake on Wan** capabilities.

> If you found this to be useful for you, a ⭐**STAR** to the repo would be awesome.

## A bit of context

Today, we are creating an _interactive web panel_ for **waking up your devices** but with a grain of salt.

While we always talk about _WoL_, we normally don't talk about what happens when you are **outside your network**.

The so-called "**Wake on Wan**" is something I have been doing for quite some while now via different methods like _SSH snippets and Amazon Alexa_.

  

----------

### Alternatives

> While [_Amazon Alexa's WoL tool_](https://www.amazon.com/Oscar-Penelo-Wake-Lan-WoL/dp/B07PGKK416) is quite convenient, it's a **paid option** _once you reach a specific amount of devices or for some extra functions._

I have it and still plan to use it from time to time, but adding new devices is a _bit of a hassle_, it requires **two third-party services** to be up and let's be honest, for the "I control my devices" kind of user, it's not a viable option at all.

_JuiceSSH_ is also a very convenient way of executing a "**wol.sh**" script, but again, snippets are a _paid feature_.

While **cloud** is getting more and more common, having solutions for us that we have our own **on-premise HomeLab** is always a pro, since we really "**own**" our infrastructure.

  

----------

## Deploying our Application

> In this section, we are going to **expose** a **Spring Boot application** written in **Java 1.8** (for _legacy support_), deploy, containerize and secure its access.

  

### What we need

-   A **Linux** based computer (I am using an [ODROID-XU4](https://wiki.odroid.com/odroid-xu4/odroid-xu4))
    
-   **Wakeonlan** packet installed
    
-   [Java JDK/JRE 1.8 or above](https://www.oracle.com/es/java/technologies/javase/javase8u211-later-archive-downloads.html)
    

  

### Recommended but not mandatory

-   [IntelliJ IDEA](https://www.jetbrains.com/idea/)
    
-   **Sreeen**, **NoHup** or **Tmux** packet installed
    
-   Ability to **port-forward** and open up ports
    
-   Ability to **VPN or Tunnel** your connection
    
-   **Docker**
    
-   **Certificate or OpenSSL Self-Signed Certificate**
    

  

### Let's start with a basic setup (we will secure, dockerize... later)

We are going to first copy our "**wol.jar"** file to a folder in our Linux box and switch to that folder

![](https://media.licdn.com/dms/image/v2/D4E12AQGSZ3uBdJobKg/article-inline_image-shrink_400_744/article-inline_image-shrink_400_744/0/1726571623901?e=1732147200&v=beta&t=7EBZgbBgvYvj5J9ga7Slj2cH7-ZJuKTxwYi_Z2-Xkow)

WinSSH File Transfer

![](https://media.licdn.com/dms/image/v2/D4E12AQEkSV7th8jrIg/article-inline_image-shrink_1500_2232/article-inline_image-shrink_1500_2232/0/1726570508467?e=1732147200&v=beta&t=A3o4PDFL-IOLdzYkEqq7pPJGhMiKBwRrvUa7tCsAlgA)

  

Let's make sure that we are running a **compatible Java VM** and that we have it added to _PATH_:

![](https://media.licdn.com/dms/image/v2/D4E12AQFTj4Nhld4kdw/article-inline_image-shrink_1500_2232/article-inline_image-shrink_1500_2232/0/1726570454382?e=1732147200&v=beta&t=eFtbp4zzUu4pNcoYpjt5L-dt6EJpkwP-_7PoFlSlnhc)

  

Let's start a **background process** with our Spring Boot application, since I'm running SSH, I need the app to _stay deployed once the SSH session is terminated_. I will use **screen** package for this.

So we run "**screen"** command and skip the documentation by pressing **Enter** or **Space**.

Once _inside a screen session_ we run our ***.jar** file:

![](https://media.licdn.com/dms/image/v2/D4E12AQF94map9Ghwdg/article-inline_image-shrink_400_744/article-inline_image-shrink_400_744/0/1726571263123?e=1732147200&v=beta&t=Cz3FD8TXMfYN3C8QUaBpocWV7fjxGAkShkE_q24YH7A)

> sudo java -jar wol.jar

By accessing our **device's ip** using **port 7800** (_default_) using our favourite web browser, we will be presented with this landing login page:

![](https://media.licdn.com/dms/image/v2/D4E12AQH6-0LQ8Se0EQ/article-inline_image-shrink_1500_2232/article-inline_image-shrink_1500_2232/0/1726571882500?e=1732147200&v=beta&t=6loJYVU6Cv43-4jJBii-SUH9sci_ci580zuRemskFt4)

  

Configuration for this login page can be located in our **application.properties:**

![](https://media.licdn.com/dms/image/v2/D4E12AQFdZPyEYv2hcg/article-inline_image-shrink_1500_2232/article-inline_image-shrink_1500_2232/0/1726572017555?e=1732147200&v=beta&t=AGpRoIos6_7RT4tJGwOApmay36SYmGc31Vu2921OmEY)

  

Once we successfully log in, we will see our configured machines and their online status:

![](https://media.licdn.com/dms/image/v2/D4E12AQHb117iLQQpsA/article-inline_image-shrink_1000_1488/article-inline_image-shrink_1000_1488/0/1726572193921?e=1732147200&v=beta&t=gJznJlAZEmRPT3CrTHskWOZIQdKluHPKwfIvPZE2iCU)

  

We can then turn on our devices by clicking on the power button of each machine, we will be prompted before turning them on to prevent accidentally powering on a device.

![](https://media.licdn.com/dms/image/v2/D4E12AQGLniESHr0kIw/article-inline_image-shrink_1500_2232/article-inline_image-shrink_1500_2232/0/1726572339824?e=1732147200&v=beta&t=a2b-q_Zkwb3xRSstsSFwKLPlRB5IQ3DrmXCTJEqLfTc)

  

> Once our device **turns on** and is **visible network-wise** to our web server, we will see that the **icon will automatically change to a green-bar signal icon.**

### Adding new devices

![](https://media.licdn.com/dms/image/v2/D4E12AQHCTnIlFNgfeQ/article-inline_image-shrink_1500_2232/article-inline_image-shrink_1500_2232/0/1726572679395?e=1732147200&v=beta&t=WKxvsYZLVdLs4NO5WFA5AUJa9kRBoJNfD100RAEd84A)

  

There is a file in resources called "**_devices.json_**" where you can _add, edit or delete devices_.

The file follows this structure, we can omit the _status_ value but I am adding it for coherence:

```
[ {
    "description": "ARM Linux Server",
    "id": 3,
    "ip": "192.168.12.140",
    "mac": "00:1e:06:32:1b:9e",
    "name": "ODROID-XU4",
    "status": false
} ]
```

> We are **all done** here, we learned how to _deploy, change user/password, manage devices_ and have our service _running in the background_.

  

----------

## Some Extras

----------

### Port-Forwarding

> In order to access our device from outside our network, which is the main attractive of our webapp, we will need to expose this port to the internet.

> No worries as those tech savvy will be able to implement a tunnel to their internal network so as to not expose this web panel unless you tunnel in!

The first step here is to get the **local IP** of the device running our **WoL Web Service**:

![](https://media.licdn.com/dms/image/v2/D4E12AQGv2n0IQPXR3w/article-inline_image-shrink_1500_2232/article-inline_image-shrink_1500_2232/0/1726573482244?e=1732147200&v=beta&t=b5FKYFHjmI-_RXnNKFL3jsGPxRy_wNxubzyo_Ltb3AI)

  

We have to **add a rule on our router** targeting our **internal device's IP** address:

![](https://media.licdn.com/dms/image/v2/D4E12AQEijWWCzca9xA/article-inline_image-shrink_400_744/article-inline_image-shrink_400_744/0/1726573691776?e=1732147200&v=beta&t=YpNAtpxLbBSAs-DZRWMen8Gz5TXUzdz8MA6KS7e4lvY)

  

> Then, if everything went well and the _service is up_, by using our **external IP** address we should be able to **_access our Wake on Lan (Wan), web panel_**.

----------

### VPN / Tunneling the Connection

One of the ways we can secure our connection is by using a **VPN client** or an **SSH Tunnel**.

In this example I am going to be using **_yet another one of my tools_, SSH Tunnel Manager**.

I have a user set up **only for proxying** on my **ODROID-XU4**, I will be showing an example on _how to do it with my current setup_, but since this is a bit more complex **_it's a bit out of scope for this article_**. You can _find more info_ on the **project's GitHub** pinned here or on my **web portfolio's** _personal project's section_:

[https://elmodo7.github.io/](https://elmodo7.github.io/)

So, since I have a connection already configured to the same device I am deploying this tool to, I can just create a **local tunnel** with this _simple configuration_:
![enter image description here](https://media.licdn.com/dms/image/v2/D4E12AQEBMXtbWk0HEA/article-inline_image-shrink_1500_2232/article-inline_image-shrink_1500_2232/0/1726576749914?e=1732147200&v=beta&t=Uf9AIbtRgLvM1BFTdc1jXgFQLVmawqdEp7DDO4VQqDQ)

![enter image description here](https://media.licdn.com/dms/image/v2/D4E12AQGQnkl1yvIRWQ/article-inline_image-shrink_1500_2232/article-inline_image-shrink_1500_2232/0/1726576686635?e=1732147200&v=beta&t=UFr-WcVB3Y270kKTjHO0L5TNgGCV-Z7OUuxwBeCaboQ)
I will just click on  **Run Tunnel**  to establish a tunnel that  **secures the connection**  towards the WoL Web Panel:
![enter image description here](https://media.licdn.com/dms/image/v2/D4E12AQGiaxP14nGn0w/article-inline_image-shrink_1500_2232/article-inline_image-shrink_1500_2232/0/1726576674745?e=1732147200&v=beta&t=yTXBwhiUqH1f1v4CVRz6fzAKavRZ6B6gHJ1qbvXkFa8)
![](https://media.licdn.com/dms/image/v2/D4E12AQHiFqGyBy4SHg/article-inline_image-shrink_1000_1488/article-inline_image-shrink_1000_1488/0/1726574878318?e=1732147200&v=beta&t=NXs2m8-05mjsn17ilS1dNQnK4GzFoN87ooaIrILCWKE)

  

Now, by accessing [localhost](http://localhost/), via the **port 8888** we are actually accessing our device's **internal IP** at **port 7800**. So we are essentially **redirecting** our traffic **securely via SSH.**

> Notice, that the URL doesn't say HTTP, but **HTTPS**. In the next steps we will be learning how to add self-signed certificates to our Spring Boot application to further encrypt our transfered data.

----------

### Adding HTTPS to your Spring Boot Web app via OPENSSL's Self-Signed Certificate

We won't get too much into the details here on **creating a self-signed certificate** since that is _not the scope of this article_, but you can follow [this _great guide_ on **_how to create your own self-signed certificate_**](https://www.baeldung.com/openssl-self-signed-cert).

In order to **_add our newly created certificate to our Spring Boot application_** we will add this configuration to our [**application.properties**](http://application.properties/):

> Yes, it really was that _simple_ to **add HTTPS** to our **Web Panel**.

----------

### Dockerizing our Spring Boot Application

I was wanting to add a full step-by-step solution on this topic, but let's be honest, there are thousands of tutorials on how to **_dockerize a Spring Boot application_**.

Since our app is built in a **self-contained jar file**, we just need to _download our favourite flavour of linux image, expose one port_, and have our app do a **_java -jar wol.jar_** on our container startup.

[This guide covers **pretty much everything I noted just now.**](https://www.baeldung.com/dockerizing-spring-boot-application)

----------

## Final Thoughts

> There are may be **many ways** to power on your devices when you are not at home, but we developed **our own solution** for this task that just works and **_provides a high security while having full control over it_**.
