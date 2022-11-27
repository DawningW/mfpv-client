var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');

function initializeCoreMod() {
    return {
        'EntityRenderDispatcherTransformer': {
            'target': {
                'type': 'CLASS',
                'name': 'net.minecraft.client.renderer.entity.EntityRenderDispatcher'
            },
            'transformer': function (classNode) {
                classNode.methods.forEach(function (methodNode) {
                    if (methodNode.name.equals("m_6213_") || methodNode.name.equals("onResourceManagerReload")) {
                        var insnNode = ASMAPI.findFirstInstruction(methodNode, Opcodes.RETURN);
                        var insnNodeLoad = new VarInsnNode(Opcodes.ALOAD, 2);
                        var insnNodeCall = ASMAPI.buildMethodCall("com/gluecode/fpvdrone/Main", "setEntityRendererProvider",
                            "(Lnet/minecraft/client/renderer/entity/EntityRendererProvider$Context;)V", ASMAPI.MethodType.STATIC);
                        methodNode.instructions.insertBefore(insnNode, insnNodeLoad);
                        methodNode.instructions.insertBefore(insnNode, insnNodeCall);
                    }
                });
                return classNode;
            }
        }
    };
}
