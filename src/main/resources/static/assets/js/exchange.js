const {createApp} = Vue

createApp({
    data(){
        return{
            id: 0,
            name: "",
            firstName: "",
            lastName: "",
            picture: "",
            allAccount:[],
            account: [],
            number: "",
            currencyExchange: [],
            currencyFinal: [],
            currencyType: "",
            amount: 0,
            amountFinal: 0,
            error:""
        }
    },
    created(){
        this.paramLocation = location.search
        this.params = new URLSearchParams(this.paramLocation)
        this.id = this.params.get("id")
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
                this.allAccount = res.data.account
                this.account = res.data.account.filter(account => account.id == this.id)
                this.number = this.account[0].number
            })
            fetch('https://v6.exchangerate-api.com/v6/a9ad510740c2d814334acd73/latest/USD')
            .then(res => res.json())
            .then(data => {
                this.currencyExchange = data.conversion_rates 
            })
            .catch(error => console.error(error))
        },
        logOut(){
            axios.post('/api/logout')
            .then(response => window.location.href = "./index.html")
        },
        textImageModalQuestion(image, message, id){
            let alertTxt = document.getElementById(id)
            alertTxt.innerHTML = `
                    <lottie-player src="${image}"  background="transparent"  class="img__lottieAlert" speed=".8" autoplay></lottie-player>
                    <p class="text-center">${message}</p>
                `
        },
        reload(){
            setTimeout(()=>{
                location. reload()
            }, 3500);
        },
        hiddenAlert(id){
            document.getElementById(id).classList.toggle("hidden__container")
        },
        closeAlert(){
            document.getElementById("alertQuestion").classList.toggle("hidden__container")
        },
        showQuestion(){

            if(this.currencyFinal.length > 0){
                this.currencyType = this.currencyFinal[0]
                this.amountFinal = (this.currencyFinal[1] * this.amount).toFixed(2)  
            }
            this.hiddenAlert("alertQuestion")
            document.getElementById("questionYesOrNo").classList.remove("hidden__container")
            this.textImageModalQuestion("https://assets6.lottiefiles.com/packages/lf20_LIU4vHuu1W.json", "Are you sure to made an exchange?", 'alertInnerLottie')
        },
        questionCreatedCardResponse(response){
            this.questionCreatedCard = response       
            this.exchange()                                  
        },        
        exchange(){        
            this.error = "" 
            if(this.questionCreatedCard == "true"){
                axios.post("/api/clients/current/account/exchange", `id=${this.id}&typeOfMoney=${this.currencyType}&type=SAVING&amountExchange=${this.amountFinal}&amount=${this.amount}`,
                {headers:{'content-type':'application/x-www-form-urlencoded'}} )
                .then(res =>{           
                    this.textImageModalQuestion("https://assets1.lottiefiles.com/packages/lf20_51yyasis.json",res.data, 'alertInnerLottie')
                    document.getElementById("questionYesOrNo").classList.add("hidden__container")
                    this.currencyFinal= []
                    this.amount= 0
                    this.amountFinal= 0
                })
                .catch(err=>{
                    this.hiddenAlert("alertQuestion")
                    this.error = err.response.data
                })
            }else{
                this.textImageModalQuestion('https://assets9.lottiefiles.com/temp/lf20_QYm9j9.json', "You don't made an exchange.", 'alertInnerLottie')
                document.getElementById("questionYesOrNo").classList.add("hidden__container")
            }     
        }
       
    }
}).mount("#app")