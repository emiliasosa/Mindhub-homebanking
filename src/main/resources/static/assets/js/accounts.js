const {createApp} = Vue
createApp({
    data(){
        return{
            id: "",
            firstName: "",
            lastName: "",
            email:"",
            picture: "",
            accounts: [],
            auxEmail: true,
            auxMoney: true,
            loans: [],
            auxSlider: 0,
            moveX: 0,
            balanceNewAccount: 0,
            numberNewAccount: "",
            date: undefined,
            numberActualAccount: 1,
            questionCreatedCard: "",
            loansArray: [],
            loanAvailableArray: [],
            loanAvailable: "",
            numbers: [1,2,3],
            transactionsId: 0,
            action: undefined,
            type: "",
            accountId: 0,
            allLoans: [],
            finalAccounts: []
        }
   },
   created(){
    this.size = window.screen.width
    this.loadData()
   },
   methods:{
        loadData(){
            axios.get(`http://localhost:8080/api/clients/current`)
            .then(res => {
                this.firstName = res.data.firstName
                this.lastName = res.data.lastName
                this.email = res.data.email
                this.accounts = res.data.account.sort((a, b) => a.id - b.id);
                this.finalAccounts = this.accounts
                this.loans = res.data.loans.sort((a, b) => a.id - b.id);
                this.picture = res.data.picture
            })

            axios.get(`/api/clients/current/typeOfloan`)
            .then(res => {
                this.allLoans = res.data
                for(loan of res.data){
                    this.loansArray.push(loan.name) 
                }

                for(loan of this.loansArray){
                    if(!this.loans.map(loans => loans.name.toUpperCase()).includes(loan.toUpperCase())){
                        this.loanAvailableArray.push(loan) 
                    }
                }
                this.loanAvailable = this.loanAvailableArray.join()
            })

           
        },
        slider(placeToMove){
            const slider = document.getElementById('cards__container')
            if(placeToMove === 'left' && this.auxSlider > 0){
                this.moveX -= document.getElementById('carrusel').offsetWidth
                slider.style.transform = `translateX(-${this.moveX}px)`;
                this.auxSlider -= 1
                if(this.numberActualAccount > 1){
                    this.numberActualAccount -= 1
                }
            }else if(placeToMove === 'rigth' && this.auxSlider < this.accounts.length -1){
                this.moveX += document.getElementById('carrusel').offsetWidth 
                slider.style.transform = `translateX(-${this.moveX}px)`;
                this.auxSlider += 1
                if(this.numberActualAccount >= 1 && this.numberActualAccount < 4 ){
                    this.numberActualAccount += 1
                }
            }        
        },
        ocultMoney(){
            this.auxMoney = true
        },
        seeMoney(id){
            for(account of this.accounts){
                if(id == account.id){
                    this.auxMoney = false
                }  
            }        
        },
        logOut(){
            axios.post('/api/logout')
            .then(res => window.location.href = "./index.html")
        },
        hiddenAlert(id){
            document.getElementById(id).classList.toggle("hidden__container")
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
            }, 3000);
        },
        showQuestion(action, id){
            if(id){
                this.accountId = id
            }
            this.hiddenAlert("alertCreatedCard")
            document.getElementById("questionYesOrNo").classList.remove("hidden__container")
            document.getElementById("alertInnerMessageCreateAccount").classList.add("hidden__container")
            if(action === "add"){
                this.textImageModalQuestion("https://assets6.lottiefiles.com/packages/lf20_LIU4vHuu1W.json", "Are you sure to add another account?", 'alertInnerLottie')
                this.action = "addPrevious"
            }else{
                this.textImageModalQuestion("https://assets6.lottiefiles.com/packages/lf20_LIU4vHuu1W.json", "Are you sure to delete this account?", 'alertInnerLottie')
                this.action = "delete"
            }            
        },
        questionCreatedCardResponse(response, action){
            this.questionCreatedCard = response
            if(action){
                this.action = action
            }
            if(this.action === "addPrevious"){
                if(this.questionCreatedCard == 'false'){
                    this.addAccount()
                } 
                this.hiddenAlert("questionYesOrNo")
                this.hiddenAlert("alertInnerMessageCreateAccount")                            
            }else if(this.action === "delete"){
                this.delete()
            }if(this.action === "add"){
                this.addAccount()
            }           
        },        
        addAccount(){
            this.hiddenAlert("alertInnerMessageCreateAccount")
            this.hiddenAlert("alertCreatedCard")
            setTimeout(()=>{
                if(this.questionCreatedCard === 'true'){
                    axios.post("/api/clients/current/accounts",`type=${this.type}`,
                    {headers:{'content-type':'application/x-www-form-urlencoded'}} )
                    .then(res =>{
                        this.loadData()
                        this.hiddenAlert("alertCreatedCard")
                        this.textImageModalQuestion('https://assets1.lottiefiles.com/packages/lf20_51yyasis.json', res.data, 'alertInnerLottie')
                        document.getElementById("questionYesOrNo").classList.add("hidden__container")
                    } )
                    .catch(err=>{
                        this.hiddenAlert("alertCreatedCard")
                        this.textImageModalQuestion('https://assets9.lottiefiles.com/temp/lf20_QYm9j9.json',err.response.data, 'alertInnerLottie')
                        document.getElementById("questionYesOrNo").classList.add("hidden__container")
                    })                                    
                }else if(this.questionCreatedCard === 'false'){
                    this.hiddenAlert("alertCreatedCard")
                    this.textImageModalQuestion('https://assets9.lottiefiles.com/temp/lf20_QYm9j9.json', "You don't created an account.", 'alertInnerLottie')
                    document.getElementById("questionYesOrNo").classList.add("hidden__container")
                }
            }, 500);    
        },
        redirectTransfer(id){
            window.location.href = `./transfers.html?id=${id}`
        },
        delete(){
            setTimeout(()=>{
                if(this.questionCreatedCard === 'true'){
                    axios.patch(`/api/clients/current/account/delete`,`disable=true&id=${this.accountId}`,
                    {headers:{'content-type':'application/x-www-form-urlencoded'}})
                    .then(res=> {
                        this.textImageModalQuestion('https://assets1.lottiefiles.com/packages/lf20_51yyasis.json', res.data, 'alertInnerLottie')
                        document.getElementById("questionYesOrNo").classList.add("hidden__container")   
                        this.finalAccounts = this.accounts.filter(account => account.id != this.accountId)
                        this.loadData()                   
                    })
                    .catch(err => {
                        this.textImageModalQuestion('https://assets9.lottiefiles.com/temp/lf20_QYm9j9.json',err.response.data, 'alertInnerLottie')
                        document.getElementById("questionYesOrNo").classList.add("hidden__container")
                    })
                }else if(this.questionCreatedCard === 'false'){
                    this.textImageModalQuestion("https://assets9.lottiefiles.com/temp/lf20_QYm9j9.json", "You don't delete the account.", 'alertInnerLottie')
                    document.getElementById("questionYesOrNo").classList.add("hidden__container")
                }
            }, 500);
        },
    }
}).mount("#app")