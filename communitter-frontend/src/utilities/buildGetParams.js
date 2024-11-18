export const buildGetParams=(interests,getParams)=>{
    let ids="";
    interests.forEach((element,i) => {
        ids=ids+`${element.id}`;
        if(i<interests.length-1) ids=ids+"|"
    });

    getParams.ids=ids;
    
}