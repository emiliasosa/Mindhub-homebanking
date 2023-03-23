const {createApp} = Vue

createApp({
    data(){
        return{
            name: "",
            firstName: "",
            lastName: "",
            maxAmount: "",
            payment: [],
            interest:"",
            nameLoan: "",
            picture: "",
            questionCreatedCard: "",
            error: "",
            description: ""
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
            document.getElementById("questionYesOrNo").classList.remove("hidden__container")
            let alertTxt = document.getElementById('alertInnerLottie')
            alertTxt.innerHTML = `
                <lottie-player src="https://assets6.lottiefiles.com/packages/lf20_LIU4vHuu1W.json"  background="transparent"  speed=".5"  class="img__lottieAlert-setting"  autoplay></lottie-player>
                `
        },
        reload(){
            setTimeout(()=>{
                location. reload()
            }, 3000);
        },
        textImageModalQuestion(image, message, id, loop, margin){
            let alertTxt = document.getElementById(id)
            alertTxt.innerHTML = `
                    <lottie-player src="${image}"  background="transparent"  class="img__lottieAlert" ${loop} speed=".8" autoplay></lottie-player>
                    <p class="text-center ${margin}">${message}</p>
                `
        },
        questionCreatedCardResponse(response){
            this.questionCreatedCard = response
            this.createLoan()
        },
        createLoan(){
            this.hiddenAlert()
            setTimeout(()=>{
                if(this.questionCreatedCard === 'true'){
                    axios.post(`/api/admin/create-loan`,
                    `name=${this.nameLoan}&maxAmount=${this.maxAmount}&payments=${this.payment}&interest=${this.interest}&description=${this.description}`,
                    {headers:{'content-type':'application/x-www-form-urlencoded'}})
                    .then(res=> {
                        this.hiddenAlert()
                        document.getElementById("questionYesOrNo").classList.add("hidden__container")
                        this.textImageModalQuestion("https://assets1.lottiefiles.com/packages/lf20_51yyasis.json", res.data, 'alertInnerLottie')
                    })
                    .catch(err => {
                        console.log(err)
                        this.error = err.response.data

                    })
                    
                }else if(this.questionCreatedCard === 'false'){
                    this.hiddenAlert()
                    document.getElementById("questionYesOrNo").classList.add("hidden__container")
                    this.textImageModalQuestion("https://assets9.lottiefiles.com/temp/lf20_QYm9j9.json", "You don't create a loan.", 'alertInnerLottie')
                }
            }, 500);
        },
        logOut(){
            axios.post('/api/logout')
            .then(response => window.location.href = "../web/index.html")
        },
        hiddenAlert(){
            document.getElementById("alertCreatedCard").classList.toggle("hidden__container")
        }
    }
}).mount("#app")