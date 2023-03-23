const {createApp} = Vue

createApp({
    data(){
        return{
            name: "",
            firstName: "",
            lastName: "",
            picture: "",
            error: "",
            amount: 0,
            maxAmount: 0,
            cardNumber: "",
            description: "",
            cvv: 0
        }
   },
created(){
    this.loadData()
    },
    methods:{
        loadData(){
            axios.get(`/api/clients/current/`)
            .then(res => {
                this.name = res.data.firstName + " " + res.data.lastName
                this.firstName = res.data.firstName
                this.lastName = res.data.lastName
                this.picture = res.data.picture
            })
        },
        logOut(){
            axios.post('/api/logout')
            .then(response => window.location.href = "./index.html")
        },
        hiddenAlert(id){
            document.getElementById(id).classList.toggle("hidden__container")
        },
        closeAlert(){
            document.getElementById("alertCreatedCard").classList.toggle("hidden__container")
            setTimeout(()=>{
                location.reload()
             }, 100);
        },
        textImageModalQuestion(image, message, id, loop, margin){
            let alertTxt = document.getElementById(id)
            alertTxt.innerHTML = `
                    <lottie-player src="${image}"  background="transparent"  class="img__lottieAlert" ${loop} speed=".8" autoplay></lottie-player>
                    <p class="text-center ${margin}">${message}</p>
                `
        },
        showQuestionAfter(){
            document.getElementById("questionYesOrNo").classList.remove("hidden__container")
            this.hiddenAlert("alertCreatedCard")
            let alertTxt = document.getElementById("alertInnerLottie")
            alertTxt.innerHTML = `
                <lottie-player src="https://assets6.lottiefiles.com/packages/lf20_LIU4vHuu1W.json"  background="transparent"  speed="1"  class="img__lottieAlert-setting"  autoplay></lottie-player>
                <p>Are you sure to save the changes?</p>
            `
        },
        questionCreatedCardResponse(response){
            this.questionCreatedCard = response
            this.paid()
        },
        paid(){
            this.hiddenAlert("alertCreatedCard")
            setTimeout(()=>{
                if(this.questionCreatedCard === 'true'){
                    axios.post(`/api/clients/current/postnet`,
                    {
                        amount: this.amount,
                        number: this.cardNumber,
                        description: this.description,
                        cvv: this.cvv
                    })
                    .then(res=> {
                        document.getElementById("questionYesOrNo").classList.add("hidden__container")
                        this.hiddenAlert("alertCreatedCard")
                        this.textImageModalQuestion("https://assets1.lottiefiles.com/packages/lf20_51yyasis.json", res.data, 'alertInnerLottie')
                    })
                    .catch(res => {
                        this.error = res.response.data             
                    })
                }else if(this.questionCreatedCard === 'false'){
                    this.hiddenAlert("alertCreatedCard")
                    document.getElementById("questionYesOrNo").classList.add("hidden__container")
                    this.textImageModalQuestion("https://assets9.lottiefiles.com/temp/lf20_QYm9j9.json", "You don't paid your bills", "alertInnerLottie")
                }
            }, 700);    
        }
    }
}).mount("#app")