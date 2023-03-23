const {createApp} = Vue

createApp({
    data(){
        return{
            name: "",
            firstName: "",
            lastName: "",
            picture: "",
            allAccounts: "",
            accounts: [],
            accountReceives: undefined,
            amount: "",
            description: "",
            questionCreatedCard: "",
            params: "",
            paramLocation: "",
            id: "",
            error: "",
            actualAccountNumber: "",
            ownAccountInAnother: ""
        }
   },
created(){
    this.paramLocation = location.search
    this.params = new URLSearchParams(this.paramLocation)
    this.id = this.params.get("id")
    this.loadData()
    this.checkAccount()
    },
    methods:{
        loadData(){
            axios.get(`/api/clients/current/`)
            .then(res => {
                this.name = res.data.firstName + " " + res.data.lastName
                this.firstName = res.data.firstName
                this.lastName = res.data.lastName
                this.picture = res.data.picture
                this.allAccounts = res.data.account.filter(account => account.id != this.id).sort((a,b) => {
                    if(a < b){ 
                        return -1; 
                    }else if(a > b){ 
                        return 1; 
                    }else{
                        return 0
                    }
                })
                this.accounts = this.allAccounts.map(account => account.number)
                this.actualAccountNumber = res.data.account.filter(account => account.id == this.id).map(account => account.number)[0]
                if(this.accounts.length == 0){
                    document.getElementById('own').setAttribute("disabled", "")
                }
            })
        },
        logOut(){
            axios.post('/api/logout')
            .then(response => window.location.href = "./index.html")
        },
        valueAccount(valueId, valueHiddenId, chekedHidden){
            let account = document.getElementById(chekedHidden)
            account.checked = false

            document.getElementById(valueId).classList.remove("hidden__container")
            document.getElementById(valueHiddenId).classList.add("hidden__container")
            this.accountReceives = ""
        },
        hiddenAlert(){
            document.getElementById("alertCreatedCard").classList.toggle("hidden__container")
        },
        textImageModalQuestion(image, message, id, loop, margin){
            let alertTxt = document.getElementById(id)
            alertTxt.innerHTML = `
                    <lottie-player src="${image}"  background="transparent"  class="img__lottieAlert" ${loop} speed=".8" autoplay></lottie-player>
                    <p class="text-center ${margin}">${message}</p>
                `
        },
        showQuestionAfterSendMoney(){
            document.getElementById("questionYesOrNo").classList.remove("hidden__container")
            this.hiddenAlert()
            let alertTxt = document.getElementById('alertInnerLottie')
            alertTxt.innerHTML = `
                <lottie-player src="https://assets6.lottiefiles.com/packages/lf20_LIU4vHuu1W.json"  background="transparent"  speed="1"  class="img__lottieAlert-setting"  autoplay></lottie-player>
                <p>Are you sure to do the transfer?</p>
                `
        },
        questionCreatedCardResponse(response){
            this.questionCreatedCard = response
            this.sendTransfer()
        },
        checkAccount(){
            this.ownAccountInAnother = "false"
            for(account of this.accounts){
                if(this.accountReceives == account){
                    this.errorAccountToSend = "You can't send to your account with this select."
                    this.ownAccountInAnother = "true"
                }

            }
        },
        sendTransfer(){
            this.hiddenAlert()                      
            setTimeout(()=>{
                if(this.questionCreatedCard === 'true' && this.ownAccountInAnother == "false"){
                    axios.post(`/api/clients/current/transactions/${this.id}`,
                    `id=${this.id}&amount=${this.amount}&description=${this.description}&accountToSend=${this.accountReceives}`,
                    {headers:{'content-type':'application/x-www-form-urlencoded'}})
                    .then(res=> {
                        document.getElementById("questionYesOrNo").classList.add("hidden__container")
                        this.hiddenAlert()
                        this.textImageModalQuestion("https://assets1.lottiefiles.com/packages/lf20_51yyasis.json",res.data, 'alertInnerLottie')
                    })
                    .catch(res => {
                        this.error = res.response.data
                    })
                }else if(this.questionCreatedCard === 'false'){
                    this.hiddenAlert()
                    document.getElementById("questionYesOrNo").classList.add("hidden__container")
                    this.textImageModalQuestion("https://assets9.lottiefiles.com/temp/lf20_QYm9j9.json", "You don't send the transfer.", 'alertInnerLottie')
                }
            }, 700);    
        }
    }
}).mount("#app")