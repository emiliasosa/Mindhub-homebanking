//import 'aos/dist/aos.css'

window.onscroll = function() {
    let positionScroll = window.scrollY;
    if(positionScroll > 0){
        document.getElementById("app").classList.add("curved-background__curved")
    }else if(positionScroll === 0){
        document.getElementById("app").classList.remove("curved-background__curved")
    }
    if(positionScroll > 177){
        document.getElementById("loans_index").classList.add("info__homebanking__index__position")
    }else if(positionScroll < 177){
        document.getElementById("loans_index").classList.remove("info__homebanking__index__position")
    }
    if(positionScroll > 533 ){
        document.getElementById("exchange_index").classList.add("info__homebanking__index__position")
    }else if(positionScroll < 533 ){
        document.getElementById("exchange_index").classList.remove("info__homebanking__index__position")
    }
    if(positionScroll > 896){
        document.getElementById("home_index").classList.add("info__homebanking__index__position")
    }else if(positionScroll < 896){
        document.getElementById("home_index").classList.remove("info__homebanking__index__position")
    }

  };


const {createApp} = Vue

createApp({
    data(){
        return{
            firstName: "",
            lastName: "",
            email:"",
            password: "",
            logInAccess: false,
            errorFirstName: "",
            errorLastName: "",
            errorEmail: "",
            errorPassword: "",
            errorLoginEmail: "",
            errorLoginPassword:"",
            firstNameFinal: "",
            lastNameFinal: "",
        }
   },
   created(){   
        emailToSend = this.emailToSendPassword
       
    },
    methods:{
        logIn(){
            axios.post('/api/login',`email=${this.email}&password=${this.password}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
            .then(res => {
                if(this.email === "admin@mindhub.com"){
                    window.location.href = "../admin/create-loan.html"
                }else{
                    window.location.href = "./accounts.html"
                }
                
            }) 
            .catch(res=> {
                document.getElementById('err__signin__email').innerText = ""
                document.getElementById('err__signin__password').innerText = ""
                let redInputEmail = document.getElementById('sigIn__email').classList
                let redInputPassword = document.getElementById('sigIn__password').classList

                if(this.email !== "" && redInputEmail.length > 1){
                    document.getElementById('sigIn__email').classList.toggle('form__input__error')
                }else if(this.password !== "" && redInputPassword.length > 1){
                    document.getElementById('sigIn__password').classList.toggle('form__input__error')
                }
                
                let error = (idMessage, idRed, message)=>{
                    let error = document.getElementById(idMessage)
                    error.style.opacity = "1"
                    error.innerText = message
                    let redInput = document.getElementById(idRed).classList
                    if(redInput.length == 1){
                        redInput.toggle('form__input__error')
                    }
                }
                
                if(this.email === "" &&  this.password === ""){
                    error('err__signin__email','sigIn__email', "Missing email." )
                    error('err__signin__password','sigIn__password',"Missing password." )
                }else if(this.email === ""){
                    error('err__signin__email','sigIn__email', "Missing email." )
                }else if( this.password === ""){
                    error('err__signin__password','sigIn__password',"Missing password." )
                }

                if(this.email && this.password && res.code == 'ERR_BAD_REQUEST'){
                    let error = document.getElementById('err__signin__password')
                    error.style.opacity = "1"
                    error.innerText = "Password or email doesn't match. Try again."
                }

            })
        },
        logOut(){
            axios.post('/api/logout')
            .then(response =>  window.location.href = "./index.html") 
        },
        register(){
            this.firstNameFinal = this.firstName.slice(0,1).toUpperCase() + this.firstName.slice(1).toLowerCase()
            this.lastNameFinal = this.lastName.slice(0,1).toUpperCase() + this.lastName.slice(1).toLowerCase()
            axios.post('/api/clients',`firstName=${this.firstNameFinal}&lastName=${this.lastNameFinal}&email=${this.email}&password=${this.password}&picture=`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
            .then(response => {
                axios.post('/api/login',`email=${this.email}&password=${this.password}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                .then(res => {
                    axios.post("/api/clients/current/accountsNewUser")
                    .then(res => window.location.href = "./accounts.html");
                }) 
            })
            .catch(res => {
                this.errorFirstName = ""
                this.errorLastName = ""
                this.errorEmail = ""
                this.errorPassword = ""
                if(res.response.data.split(" ")[1] === 'password'){
                    this.errorPassword = res.response.data
                }else if(res.response.data.split(" ")[1] === 'email'){
                    this.errorEmail = res.response.data
                }else if(res.response.data.split(" ")[1] === 'firstname'){
                    this.errorFirstName = res.response.data
                }else if(res.response.data.split(" ")[1] === 'lastname'){
                    this.errorLastName = res.response.data
                }else{
                    this.errorEmail = res.response.data
                }
            })
                
        },
        show(id){
            document.getElementById(id).classList.toggle('hidden__container')
            document.getElementById('main__content__hidden').classList.toggle('hidden__container')
        },
        forgetPassword(){
            document.getElementById('forgetPassword').classList.toggle('hidden__container')
        },
        sendEmail() {

           emailjs.sendForm('service_21c9j26', 'template_d0njkdq', email= document.getElementById('email_id').value, 'rlNzedtairB8FVo5o')
           .then((result) => {
               console.log('SUCCESS!', result.text);
           }, (error) => {
               console.log('FAILED...', error.text);
           });
          }
    }
}).mount("#app")