const unionOfArray = (array, key) => {
    let o = {};
    let union = [];
    for (var i=0; i<array.length; i++) {
        let a = array[i];
        if (o[a[key]] == 1) continue;
        o[a[key]] = 1;
        union.push(a);
    }
    return union;
};

export {unionOfArray};