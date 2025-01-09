import service from "@/utils/request";

const BASE_API =  import.meta.env.VITE_APP_BASE_API;

const ctrl = new AbortController()
const conversion_id = ref()
let times = 0
import { fetchEventSource } from '@microsoft/fetch-event-source'
const token = uni.getStorageSync("h5_token");
console.log("我看看 Token", token)

export default {

    downLoadWord(data) {
        return service({
            url: "/chatbot/download",
            method: "post",

            data: data,
        });
    },

    stream(formData) {
        console.log("FormData", formData)
        return fetchEventSource(BASE_API + '/chatbot',{
            method: "POST",
            openWhenHidden: true,
            headers: {
                // 'Content-Type': 'multipart/form-data; boundary=----WebKitFormBoundary',
                'Content-Type': 'application/json',
                'Cache-Control': 'no-cache',
                'Connection': 'keep-alive',
                'Authorization': `Bearer ${token}`,
            },
            signal: ctrl.signal,
            body: formData,
            onmessage: event => {
                try{

                    if (times === 0) {
                        messages.value[messages.value.length - 1].content = ''
                    }
                    times += 1
                    const newMessage = JSON.parse(event.data)

 /*                   if (newMessage.code !== 200) {
                        console.error('接口返回错误:', newMessage);
                        ctrl?.abort();  // 中止请求
                    }*/

                    if (newMessage.event === 'message') {

                        conversion_id.value = newMessage.conversation_id

                        let message = messages.value[messages.value.length - 1]

                        if (message.senderId === currentUserId.value) {

                            addNewMessage(newMessage.answer)

                        } else {
                            messages.value[messages.value.length - 1].content += newMessage.answer
                        }

                    }
                    else{
                        ctrl?.abort()
                    }
                }
                catch (e) {
                    ctrl?.abort()
                }
            }
        }).then(response => {
            console.log('收到后端数据:', response.data);
            // 处理返回的数据流
        }).catch(error => {
            console.error('请求失败:', error);
        });
    }
};
