import { createApp } from 'vue'
import { createStore } from 'vuex'
import user from "@/store/modules/user";
import app from "@/store/modules/app";
import getters from "@/store/getters";



const store = createStore({

  modules: {
    app,
    user,
  },
  getters
})


export default store