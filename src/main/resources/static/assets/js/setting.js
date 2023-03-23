const {createApp} = Vue

createApp({
    data(){
        return{
            name: "",
            firstName: "",
            lastName: "",
            email:"",
            pictureUpdate: "",
            picture: "",
            questionCreatedCard: ""
        }
   },
   created(){
    this.loadData()
   },
   methods:{
        loadData(){
            axios.get(`http://localhost:8080/api/clients/current`)
            .then(res => {
                this.name = res.data.firstName + " " + res.data.lastName
                this.firstName = res.data.firstName
                this.lastName = res.data.lastName
                this.email = res.data.email
                this.picture = res.data.picture
                this.pictureUpdate = this.picture

            })
        },
        showQuestionAfterUpdate(){
            this.hiddenAlert()
            let alertTxt = document.getElementById('alertInnerLottie')
            alertTxt.innerHTML = `
                <lottie-player src="https://assets6.lottiefiles.com/packages/lf20_LIU4vHuu1W.json"  background="transparent"  speed=".5"  class="img__lottieAlert-setting"  autoplay></lottie-player>
                `
        },
        questionCreatedCardResponse(response){
            this.questionCreatedCard = response
            this.update()
        },
        update(){
            this.hiddenAlert()
            setTimeout(()=>{
                if(this.questionCreatedCard === 'true'){
                    axios.patch(`/api/clients/current`,
                    `firstName=${this.firstName}&lastName=${this.lastName}&email=${this.email}&picture=${this.pictureUpdate}`,
                    {headers:{'content-type':'application/x-www-form-urlencoded'}})
                    .then(res=> {
                        this.hiddenAlert()
                        let alertTxt = document.getElementById('alertInnerMessage')
                        alertTxt.innerHTML = `
                            <lottie-player src="https://assets1.lottiefiles.com/packages/lf20_51yyasis.json"  background="transparent"  class="img__lottieAlert" speed=".5" autoplay></lottie-player>
                            <p>You update the changes.</p>
                        `
                    });
                    setTimeout(()=>{
                        location. reload()
                    }, 3000);
                }else if(this.questionCreatedCard === 'false'){
                    this.hiddenAlert()
                    let alertTxt = document.getElementById('alertInnerMessage')
                    alertTxt.innerHTML = `
                        <lottie-player src="https://assets9.lottiefiles.com/temp/lf20_QYm9j9.json"  background="transparent"  speed=".5"  class="img__lottieAlert"   loop autoplay></lottie-player>
                        <p>You don't upload the changes.</p>
                    `
                    setTimeout(()=>{
                        location. reload()
                    }, 3000);
                }
            }, 500);
        },
        logOut(){
            axios.post('/api/logout')
            .then(response => {
                if(this.email === "admin@mindhub.com"){
                    window.location.href = "../web/index.html"
                }else{
                    window.location.href = "./index.html"
                }
                
            })
        },
        hiddenAlert(){
            document.getElementById("alertCreatedCard").classList.toggle("hidden__container")
        }
    }
}).mount("#app")