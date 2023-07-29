import { createStore } from 'vuex'
import user from "@/store/modules/user";
import app from "@/store/modules/app";
import getters from "@/store/getters";
import ssh from "@/store/modules/ssh";



const store = createStore({

  modules: {
    app,
    user,
    ssh,
  },
  getters
})


export default store