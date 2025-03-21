const STATIC_URL= import.meta.env.VITE_STATIC_URL
const get_resource_url=(path)=>{
    return STATIC_URL+path
}
export default get_resource_url