const {createApp} = Vue

createApp({
    data(){
        return{
            name: "",
            firstName: "",
            lastName: "",
            picture: "",
            selectLoan: "",
            error: "",
            payments: [],
            paymentSelected: 0,
            amount: 0,
            eachPaymentToPay: 0,
            accountReceives: "",
            maxAmount: 0,
            accounts: [],
            radioDisable: [],
            haveLoans: [],
            haveLoansToShow: [],
            questionCreatedCard: "",
            loanId: 0,
            accountId: 0,
            allLoans: [],
        }
   },
created(){
    this.loadData()
    },
    methods:{
        loadData(){
            axios.get(`/api/clients/current/typeOfloan`)
            .then(res => {
                this.allLoans = res.data
                for(loan of this.allLoans){
                    this.radioDisable.push(loan.name)
                }
            })
            axios.get(`/api/clients/current/`)
            .then(res => {
                this.name = res.data.firstName + " " + res.data.lastName
                this.firstName = res.data.firstName
                this.lastName = res.data.lastName
                this.picture = res.data.picture
                this.allAccounts = res.data.account.sort((a,b) => {
                    if(a.id < b.id){ 
                        return -1; 
                    }else if(a > b.id){ 
                        return 1; 
                    }else{
                        return 0
                    }
                })
                this.accounts = this.allAccounts.map(account => account.number)
                this.haveLoans = res.data.loans.map(loan => loan.name)
                this.haveLoansToShow = res.data.loans.map(loan => loan.name).join().toLowerCase() 
                   
                for(loan of this.radioDisable){
                    let finalLoan = loan
                    if(this.haveLoans.includes(finalLoan)){
                        document.getElementById(finalLoan).setAttribute("disabled", "")
                    }      
                    
                }          
            })
            
            
        },
        showLoanInfo(loanToSee){
            for(radio of this.radioDisable){
                if(radio !== loanToSee){
                    document.getElementById(radio).checked = false
                }
            }
            this.paymentSelected = undefined
            this.selectLoan = loanToSee
            this.payments = this.allLoans.filter(loan => loan.name === loanToSee.toUpperCase()).map(loan => loan.payments)[0]
            this.maxAmount = this.allLoans.filter(loan => loan.name === loanToSee.toUpperCase()).map(loan => loan.maxAmount)[0]
            this.interest = this.allLoans.filter(loan => loan.name === loanToSee.toUpperCase()).map(loan => loan.interest)[0]
               
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
        textImageModalQuestion(image, message, id, loop, margin){
            let alertTxt = document.getElementById(id)
            alertTxt.innerHTML = `
                    <lottie-player src="${image}"  background="transparent"  class="img__lottieAlert" ${loop} speed=".8" autoplay></lottie-player>
                    <p class="text-center ${margin}">${message}</p>
                `
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
        showQuestion(){
            this.error = ""
            let amountWithTaxes = (((this.interest * this.amount / 100) + this.amount) / this.paymentSelected)
            this.eachPaymentToPay = amountWithTaxes.toLocaleString('en-US', {style:"currency", currency:"USD"})
            document.getElementById("questionYesOrNo").classList.remove("hidden__container")
            if(this.selectLoan && amountWithTaxes){
                document.getElementById("applyQuestion").classList.remove("hidden__container")
            }
            this.hiddenAlert("alertCreatedCard")
            let alertTxt = document.getElementById('alertInnerLottie')
            alertTxt.innerHTML = `
            <lottie-player src="https://assets6.lottiefiles.com/packages/lf20_LIU4vHuu1W.json"  background="transparent"  speed="1"  class="img__lottieAlert-setting"  autoplay></lottie-player>
            <p>Are you sure to do the loan?</p>
            `
        },
        calculatePayment(){
            this.hiddenAlert("alertCreatedCard")
            let totalAmountWithTaxes = ((this.interest * this.amount / 100) + this.amount).toLocaleString('en-US', {style:"currency", currency:"USD"})
            let amountWithTaxes = (((this.interest * this.amount / 100) + this.amount) / this.paymentSelected)
            this.eachPaymentToPay = amountWithTaxes.toLocaleString('en-US', {style:"currency", currency:"USD"})
            if(this.amount !== 0 && this.paymentSelected !== 0){
                let alertTxt = document.getElementById('alertInnerMessage')
                alertTxt.innerHTML = `
                        <lottie-player src="https://assets2.lottiefiles.com/packages/lf20_wnnjhppj.json"  background="transparent"  speed="1"  class="img__lottieAlert-setting mb-5"  loop  autoplay></lottie-player>
                        <p v-if="paymentSelected" class="secundary__text__alert text-center text-danger"><b>The total amount of the ${this.selectLoan} loan is ${totalAmountWithTaxes}.</b></p>
                        <p v-if="paymentSelected" class="secundary__text__alert text-center text-danger"><b> You're appling with ${this.paymentSelected} payments of<br> ${this.eachPaymentToPay} each one.</b></p>
                    `
            }else{
                this.textImageModalQuestion("https://assets9.lottiefiles.com/temp/lf20_QYm9j9.json", "You can't calculate the loan payment with an empty fields.", 'alertInnerLottie')
            }
        },
        questionCreatedCardResponse(response){
            this.questionCreatedCard = response
            this.applyLoan()
        },
        applyLoan(){
            this.hiddenAlert("alertCreatedCard")
            setTimeout(()=>{
                if(this.questionCreatedCard === 'true'){
                    axios.post(`/api/clients/current/loan`,
                    {
                        name: this.selectLoan,
                        amount: this.amount,
                        payment: this.paymentSelected,
                        destinationAccount: this.accountReceives
                    })
                    .then(res=> {
                        this.loadData()
                        document.getElementById("applyQuestion").classList.add("hidden__container")
                        document.getElementById("questionYesOrNo").classList.add("hidden__container")
                        this.accountId = this.allAccounts.filter(account => account.number === this.accountReceives).map(account => account.id)[0]
                        this.hiddenAlert("alertCreatedCard")
                        this.textImageModalQuestion("https://assets1.lottiefiles.com/packages/lf20_51yyasis.json", res.data, 'alertInnerLottie')
                    })
                    .catch(res => {
                        this.error = res.response.data             
                    })
                }else if(this.questionCreatedCard === 'false'){
                    this.hiddenAlert("alertCreatedCard")
                    document.getElementById("questionYesOrNo").classList.add("hidden__container")
                    this.textImageModalQuestion("https://assets9.lottiefiles.com/temp/lf20_QYm9j9.json", "You don't apply in the loan.", 'alertInnerLottie')
                }
            }, 700);    
        }
    }
}).mount("#app")